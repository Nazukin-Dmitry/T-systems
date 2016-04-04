package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.WrongSerialNumberException;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.DriverEntity;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.model.enums.DriverStatus;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import com.tsystems.nazukin.logiweb.service.api.DriverServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service that provides functionality for working with {@link DriverEntity}, that representing driver's data.
 */
@Service
@Transactional
public class DriverService implements DriverServiceApi {

    /**
     * Max work time for driver in a month.
     */
    private static final Integer MAX_WORK_TIME = 176;

    /**
     * Data access object for driver's data
     */
    private DriverDao driverDao;
    /**
     * Data access object for city's data
     */
    private CityDao cityDao;
    /**
     * Data access object for employee's data
     */
    private EmployeeDao employeeDao;

    /**
     * Constructs service.
     *
     * @param driverDao   data access object for driver's data
     * @param cityDao     data access object for citiy's data
     * @param employeeDao data access object for employee's data
     */
    @Autowired
    public DriverService(DriverDao driverDao, CityDao cityDao, EmployeeDao employeeDao) {
        this.driverDao = driverDao;
        this.cityDao = cityDao;
        this.employeeDao = employeeDao;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save(Integer employeeId, Integer cityId) {
        DriverEntity driverEntity = new DriverEntity();
        CityEntity cityEntity = cityDao.findById(CityEntity.class, cityId);
        EmployeeEntity employeeEntity = employeeDao.findById(EmployeeEntity.class, employeeId);
        Integer serialNumber = driverDao.maxSerialNumber();

        if (serialNumber == null) {
            serialNumber = 0;
        }

        driverEntity.setCurrentCity(cityEntity);
        driverEntity.setEmployee(employeeEntity);
        driverEntity.setStatus(DriverStatus.FREE);
        driverEntity.setWorkTime(0);
        driverEntity.setSerialNumber(serialNumber + 1);
        employeeEntity.setEmployeeType(EmployeeType.DRIVER);
        driverDao.save(driverEntity);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<DriverEntity> findAllByCityId(Integer id) {
        List<DriverEntity> result;
        result = driverDao.findByCityId(id);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public DriverEntity find(Integer id) {
        DriverEntity result;
        result = driverDao.findById(DriverEntity.class, id);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update(String id, String firstName, String secondName, String workTime, String cityId) {
        DriverEntity driverEntity = driverDao.findById(DriverEntity.class, Integer.parseInt(id));
        driverEntity.getEmployee().setFirstName(firstName);
        driverEntity.getEmployee().setSecondName(secondName);
        driverEntity.setWorkTime(Integer.parseInt(workTime));
        driverEntity.setCurrentCity(cityDao.findById(CityEntity.class, Integer.parseInt(cityId)));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void delete(Integer id) {
        DriverEntity entity = driverDao.findById(DriverEntity.class, id);
        ///check that driver don't have any order now
        if (entity.getCurrentOrder() != null) {
            throw new DriverAllreadyUsedException();
        }
        driverDao.delete(entity);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<DriverEntity> findForOrder(OrderEntity orderEntity) {
        Date startTime = orderEntity.getStartTime();
        Integer duration = orderEntity.getDuration();
        List<DriverEntity> drivers;
        List<DriverEntity> result = new ArrayList<>();

        drivers = driverDao.findForOrder(orderEntity.getStartCity().getId());

        //sets start time and end time of order
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startTime);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(startTime.getTime() + TimeUnit.HOURS.toMillis(duration));

        //if startTime of order is not in next month
        if (Calendar.getInstance().get(Calendar.MONTH) == calendarStart.get(Calendar.MONTH)) {
            //if end of order in the next month
            long diffHours = duration;
            if (calendarStart.get(Calendar.MONTH) != calendarEnd.get(Calendar.MONTH)) {
                //set time to start of neext month
                calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendarEnd.set(Calendar.HOUR_OF_DAY, calendarEnd.getActualMinimum(Calendar.HOUR_OF_DAY));
                calendarEnd.set(Calendar.MINUTE, calendarEnd.getActualMinimum(Calendar.MINUTE));
                calendarEnd.set(Calendar.SECOND, calendarEnd.getActualMinimum(Calendar.SECOND));

                long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                diffHours = TimeUnit.MILLISECONDS.toHours(diff);
            }

            for (DriverEntity driver : drivers) {
                if (driver.getWorkTime() + diffHours <= MAX_WORK_TIME) {
                    result.add(driver);
                }
            }
        } else {
            result = drivers;
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public DriverEntity findByEmployeeIdAndSerial(Integer employeeId, Integer serialNumber) {
        DriverEntity result;
        result = driverDao.findByEmployeeId(employeeId);
        if (result.getSerialNumber() != serialNumber) {
            throw new WrongSerialNumberException();
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<DriverEntity> findAll() {
        List<DriverEntity> result;
        result = driverDao.findAll();
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<DriverEntity> findByOrderId(Integer orderId){
        return driverDao.findByOrderId(orderId);
    }


}
