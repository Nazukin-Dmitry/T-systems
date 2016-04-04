package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.OrderIsInProgressException;
import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.entity.*;
import com.tsystems.nazukin.logiweb.model.enums.CargoStatus;
import com.tsystems.nazukin.logiweb.model.enums.OrderItemType;
import com.tsystems.nazukin.logiweb.service.api.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 1 on 20.02.2016.
 * GET /manager/orders/createStart
 */
@Controller
@RequestMapping("/manager/orders")
public class OrdersController {

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    public static final String MAX_WEIGHT = "maxWeight";
    public static final String ORDER = "order";
    public static final String LIST_ORDER_ITEMS = "listOrderItems";
    public static final String CARGO_LIST = "cargoList";
    public static final String RETURN_TO_SELECT_DRIVERS = "returnSelect";

    public final static String ERROR_DRIVER_USED = "errorDriverUsed";

    private static final int MAX_HOURS = 176;

    private static final Logger logger = Logger.getLogger(OrdersController.class);

    @Autowired
    private DriverServiceApi driverService;
    @Autowired
    private OrderServiceApi orderService;
    @Autowired
    private MapServiceApi mapService;
    @Autowired
    private TruckServiceApi truckService;
    @Autowired
    private CityServiceApi cityService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrdersStatusPage(@RequestParam(value = "cityId", required = false) String cityId,
                                      Model model) {
        CityEntity currentCity;
        List<CityEntity> cities = cityService.getAll();
        List<OrderEntity> orders;
        if (cityId != null) {
            if ("all".equals(cityId)) {
                currentCity = new CityEntity();
                currentCity.setName("All cities");
                model.addAttribute("currentCity", currentCity);
                orders = orderService.findAll();
            } else {
                currentCity = cityService.find(Integer.parseInt(cityId));
                model.addAttribute("currentCity", currentCity);
                orders = orderService.findByStartCityId(Integer.parseInt(cityId));
            }
        } else {
            orders = Collections.emptyList();
        }
        model.addAttribute("cityList", cities);
        model.addAttribute("orderList", orders);
        return "/manager/orders";
    }

    @RequestMapping(value = "/cargos", method = RequestMethod.GET)
    public String getCargosStatusPage(@RequestParam(value = "orderId", required = true) Integer orderId,
                                      Model model,
                                      @ModelAttribute("error") String error) {
        List<CargoEntity> cargoList = orderService.findCargosByOrderId(orderId);
        List<OrderItemEntity> items = orderService.findItemsByOrderId(orderId);
        OrderEntity orderEntity = orderService.findById(orderId);
        List<DriverEntity> drivers = driverService.findByOrderId(orderId);
        TruckEntity truck = truckService.findByOrderId(orderEntity.getId());
        model.addAttribute("order", orderEntity);
        model.addAttribute("orderItemList", items);
        model.addAttribute("driverList", drivers);
        model.addAttribute("cargoList", cargoList);
        model.addAttribute("truck", truck);
        return "/manager/orderCargos";
    }

    @RequestMapping("/createStart")
    public String getCreateStartPage(@RequestParam(value = "cityId", required = false) String cityId,
                                     HttpSession session,
                                     Model model,
                                     HttpServletRequest request) {
        ///if manager created new drivers he come back to select drivers
        Object returnToSelectDrivers = session.getAttribute(RETURN_TO_SELECT_DRIVERS);

        if (returnToSelectDrivers != null && (Boolean) returnToSelectDrivers) {
            request.getSession().setAttribute(RETURN_TO_SELECT_DRIVERS, false);
            return "redirect:/manager/orders/selectDrivers";
        } else {
            CityEntity currentCity;
            List<CityEntity> cities = cityService.getAll();
            List<TruckEntity> trucks;
            if (cityId != null) {
                currentCity = cityService.find(Integer.parseInt(cityId));
                request.setAttribute("currentCity", currentCity);
                trucks = truckService.findAllForOrder(Integer.parseInt(cityId));
            } else {
                trucks = Collections.emptyList();
            }
            model.addAttribute("cityList", cities);
            model.addAttribute("truckList", trucks);

            return "manager/ordersCreateStartPage";
        }
    }

    @RequestMapping(value = "/createDate", method = RequestMethod.GET)
    public String getTimePage() {
        return "manager/ordersCreateTime";
    }

    @RequestMapping(value = "/createPoint", method = RequestMethod.GET)
    public String getCreatePointPage(@RequestParam(value = "number", required = true) Integer number,
                                     Model model) {
        model.addAttribute("number", number);
        return "/manager/ordersCreateItem";
    }

    @RequestMapping(value = "/createPointCity", method = RequestMethod.GET)
    public String getCreatePointCityPage(@RequestParam(value = "number", required = true) Integer number,
                                         @RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "cityIdTo", required = false) Integer cityIdTo,
                                         Model model,
                                         HttpSession session) {
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
        //TODO maybe add запрет листания страниц назад
        List<CityEntity> cityList = cityService.getAll();
        model.addAttribute("cityList", cityList);

        CityEntity cityFrom = listOrderItems.get(number - 1).getCity();
        model.addAttribute("cityFrom", cityFrom);
        model.addAttribute("number", number);

        if (type != null) {
            switch (type) {
                case "errorCreateCity":
                    model.addAttribute("errorCreateCity", "City with same name already exists!!!");
                    break;
                case "interval":
                    CityEntity cityTo = cityService.find(cityIdTo);
                    model.addAttribute("visible", true);
                    model.addAttribute("cityTo", cityTo);
                    break;
                case "errorUnloadingCargos":
                    model.addAttribute("errorWithOrder", "Not all cargos unloaded!!!");
                    break;
                case "errorMaxHours":
                    model.addAttribute("errorWithOrder", "The duration of the order is bigger than 176 hours!!! Please recreate order.");
                    break;
                default:
                    break;
            }
        }
        return "manager/ordersCreateCity";
    }

    @RequestMapping(value = "/editOrder", method = RequestMethod.GET)
    public String getEditOrderPage(@RequestParam(value = "orderId", required = true) Integer orderId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        OrderEntity order = orderService.findById(orderId);
        List<OrderItemEntity> items = orderService.findItemsByOrderId(orderId);
        session.setAttribute(ORDER, order);
        session.setAttribute(LIST_ORDER_ITEMS, items);
        TruckEntity truck = truckService.findByOrderId(orderId);
        order.setTruck(truck);
        Integer maxWeight = truck.getCapacity();
        List<CargoEntity> cargos = new ArrayList<>();

        session.setAttribute(CARGO_LIST, cargos);
        session.setAttribute(MAX_WEIGHT, maxWeight);
        session.setAttribute(RETURN_TO_SELECT_DRIVERS, false);
        redirectAttributes.addAttribute("number", items.size());
        return "redirect:/manager/orders/createPointCity";
    }

    @RequestMapping(value = "selectDrivers", method = RequestMethod.GET)
    public String getSelectDriversPage(@RequestParam(value = "type", required = false) String type,
                                       Model model,
                                       HttpSession session) {
        if (type != null) {
            switch (type) {
                case ERROR_DRIVER_USED:
                    model.addAttribute("error", "Some of the drivers you selected are already used!!! Please, reselect.");
                    break;
                default:
                    break;
            }
        }

        OrderEntity orderEntity = (OrderEntity) session.getAttribute(ORDER);
        List<DriverEntity> driverList = driverService.findForOrder(orderEntity);
        //if we edit order
        if (orderEntity.getId() != null) {
            List<DriverEntity> driverOfThisOrderList = driverService.findByOrderId(orderEntity.getId());
            driverList.addAll(driverOfThisOrderList);
        }

        model.addAttribute("duration", orderEntity.getDuration());
        model.addAttribute("driverNumber", orderEntity.getTruck().getDriverCount());
        model.addAttribute("driverList", driverList);

        if ((Boolean) session.getAttribute(RETURN_TO_SELECT_DRIVERS)) {
            session.setAttribute(RETURN_TO_SELECT_DRIVERS, false);
        }
        return "/manager/ordersSelectDrivers";
    }


    @RequestMapping(value = "/createResult", method = RequestMethod.GET)
    public String getResultPage(@RequestParam(value = "type", required = true) String type,
                                @RequestParam(value = "id", required = false) String orderId,
                                Model model,
                                @ModelAttribute("errorMessage") String message) {
        model.addAttribute("type", type);
        model.addAttribute("orderId", orderId);

        return "/manager/ordersCreateResult";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteOrder(@RequestParam(value = "id", required = true) Integer orderId,
                              @RequestParam(value = "cityId", required = true) Integer cityId,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.delete(orderId);
            redirectAttributes.addAttribute("cityId", cityId);
            return "redirect:/manager/orders";
        } catch (OrderIsInProgressException ex) {
            logger.warn("Try to delete order in progress!!!", ex);
            redirectAttributes.addFlashAttribute("error", "You can not delete an order in progress!!!");
            redirectAttributes.addAttribute("orderId", orderId);
            return "redirect:/manager/orders/cargos";
        }
    }

    @RequestMapping(value = "/back", method = RequestMethod.POST)
    public String backToPreviousOrderItem(@RequestParam(value = "itemNum", required = true) Integer itemNum,
                                          @RequestParam(value = "action", required = true) String action,
                                          CargoEntity cargo,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        List<OrderItemEntity> orderItems;
        switch (action) {
            case "delete":
                orderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
                OrderEntity order = (OrderEntity) session.getAttribute(ORDER);
                List<CargoEntity> cargos = new ArrayList<>();
                orderItems.removeIf(orderItemEntity -> orderItemEntity.getSequenceNumber() > itemNum);
                Integer weight = 0;
                for (OrderItemEntity item : orderItems) {
                    if (item.getSequenceNumber() <= itemNum) {
                        if (item.getType() == OrderItemType.LOADING) {
                            weight += item.getCargo().getWeight();
                            cargos.add(item.getCargo());
                        } else if (item.getType() == OrderItemType.UNLOADING) {
                            weight -= item.getCargo().getWeight();
                            cargos.remove(item.getCargo());
                        }
                    }
                }
                session.setAttribute(MAX_WEIGHT, order.getTruck().getCapacity() - weight);
                session.setAttribute(CARGO_LIST, cargos);
                redirectAttributes.addAttribute("number", itemNum + 1);
                return "redirect:/manager/orders/createPointCity";
            case "update":
                orderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
                Integer maxWeight = (Integer) session.getAttribute(MAX_WEIGHT);
                OrderItemEntity item = orderItems.get(itemNum);
                if (cargo.getWeight() > maxWeight) {
                    throw new IllegalArgumentException("Cargo weight > max weight!!!");
                }
                session.setAttribute(MAX_WEIGHT, maxWeight - cargo.getWeight() + item.getCargo().getWeight());
                item.getCargo().setName(cargo.getName());
                item.getCargo().setWeight(cargo.getWeight());
                return "redirect:" + request.getHeader("Referer");
            default:
                return "redirect:" + request.getHeader("Referer");
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws ParseException {
        String action = request.getParameter("action");

        switch (action) {
            case "chooseCargo":
                return chooseCargo(request);

            case "time":
                return setTime(request);

            case "createTransit":
                return createTransit(request);

            case "createLoading":
                return createLoading(request);

            case "createUnloading":
                return createUnloading(request);

            case "chooseCity":
                return chooseCity(request);

            case "createCity":
                return createCity(request);

            case "createInterval":
                return createInterval(request);

            case "completeOrder":
                return completeOrder(request);

            /// save order in database
            case "selectDrivers":
                return selectDriversAndSaveOrder(request, redirectAttributes);

            case "createDrivers":
                return createDrivers(request);

            default:
                return "";
        }
    }

    public String chooseCargo(HttpServletRequest request) {
        OrderEntity order = new OrderEntity();
        String truckId = request.getParameter("truckId");
        String cityId = request.getParameter("cityId");

        CityEntity cityEntity = cityService.find(Integer.parseInt(cityId));
        TruckEntity truck = truckService.find(Integer.parseInt(truckId));
        order.setTruck(truck);
        order.setStartCity(cityEntity);

        List<OrderItemEntity> listOrderItems = new ArrayList<>();
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setCity(cityEntity);
        orderItemEntity.setSequenceNumber(0);
        listOrderItems.add(0, orderItemEntity);

        List<CargoEntity> cargoList = new ArrayList<>();

        HttpSession session = request.getSession();
        session.setAttribute(CARGO_LIST, cargoList); ///cargos to unloading
        session.setAttribute(LIST_ORDER_ITEMS, listOrderItems); ///items of order
        session.setAttribute(ORDER, order); ///order
        session.setAttribute(MAX_WEIGHT, truck.getCapacity());
        session.setAttribute(RETURN_TO_SELECT_DRIVERS, false);

        return "redirect:/manager/orders/createDate";
    }

    public String setTime(HttpServletRequest request) throws ParseException {
        OrderEntity order = (OrderEntity) request.getSession().getAttribute(ORDER);
        String dateS = request.getParameter("date");
        Date date = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse(dateS);
        order.setStartTime(date);
        return "redirect:/manager/orders/createPoint?number=0";
    }

    public String createTransit(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) request.getSession()
                .getAttribute(LIST_ORDER_ITEMS);
        OrderItemEntity item = listOrderItems.get(number);
        item.setType(OrderItemType.TRANSIT);
        return "redirect:/manager/orders/createPointCity?number=" + (number + 1);
    }

    public String createLoading(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        Integer cargoWeight = Integer.parseInt(request.getParameter("cargoWeight"));
        String cargoName = request.getParameter("cargoName");
        HttpSession session = request.getSession();
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
        List<CargoEntity> cargoList = (List<CargoEntity>) session.getAttribute(CARGO_LIST);

        OrderItemEntity item = listOrderItems.get(number);
        item.setType(OrderItemType.LOADING);

        CargoEntity cargo = new CargoEntity();
        cargo.setName(cargoName);
        cargo.setStatus(CargoStatus.PREPARED);
        cargo.setWeight(cargoWeight);
        item.setCargo(cargo);

        cargoList.add(cargo);

        //update maxWeight it should be less
        Integer maxWeight = (Integer) session.getAttribute(MAX_WEIGHT);
        if (cargo.getWeight() > maxWeight) {
            throw new IllegalArgumentException("Cargo weight > max weight!!!");
        }
        session.setAttribute(MAX_WEIGHT, maxWeight - cargoWeight);

        return "redirect:/manager/orders/createPointCity?number=" + (number + 1);
    }

    public String createUnloading(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        Integer cargoNumber = Integer.parseInt(request.getParameter("cargoNumber")); ///cargo to unload
        HttpSession session = request.getSession();
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
        List<CargoEntity> cargoList = (List<CargoEntity>) session.getAttribute(CARGO_LIST);

        OrderItemEntity item = listOrderItems.get(number);
        item.setType(OrderItemType.UNLOADING);
        //find cargo to unloading
        CargoEntity cargoToUnloading = cargoList.get(cargoNumber);
        //set cargo to unloading
        item.setCargo(cargoToUnloading);
        //remove this cargo
        cargoList.remove(cargoNumber.intValue());

        //update maxWeight, it should be bigger after unloading
        Integer maxWeight = (Integer) session.getAttribute(MAX_WEIGHT);
        session.setAttribute(MAX_WEIGHT, maxWeight + cargoToUnloading.getWeight());

        return "redirect:/manager/orders/createPointCity?number=" + (number + 1);
    }

    public String chooseCity(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        Integer cityIdTo = Integer.parseInt(request.getParameter("cityTo"));

        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) request.getSession()
                .getAttribute(LIST_ORDER_ITEMS);
        Integer cityIdFrom = listOrderItems.get(number - 1).getCity().getId();

        Integer interval = mapService.getInterval(cityIdTo, cityIdFrom);
        if (interval == null) {
            return "redirect:/manager/orders/createPointCity?number=" + number + "&type=interval&cityIdTo="
                    + cityIdTo + "&cityIdFrom=" + cityIdFrom;
        }

        CityEntity cityEntityTo = cityService.find(cityIdTo);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setCity(cityEntityTo);
        orderItemEntity.setSequenceNumber(number);
        listOrderItems.add(orderItemEntity);

        return "redirect:/manager/orders/createPoint?number=" + number;
    }

    public String createCity(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        String cityToName = request.getParameter("cityName");
        Integer interval = Integer.parseInt(request.getParameter("interval"));
        Integer cityFromId = Integer.parseInt(request.getParameter("cityFrom"));
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) request.getSession().getAttribute(LIST_ORDER_ITEMS);

        try {
            CityEntity cityTo = mapService.save(cityFromId, cityToName, interval);
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setCity(cityTo);
            orderItemEntity.setSequenceNumber(number);
            listOrderItems.add(orderItemEntity);

            return "redirect:/manager/orders/createPoint?number=" + number;
        } catch (RuntimeException e) {
            logger.warn("Try to save city with existing name", e);
            return "redirect:/manager/orders/createPointCity?number=" + number + "&type=errorCreateCity";
        }
    }

    public String createInterval(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        Integer cityFromId = Integer.parseInt(request.getParameter("cityFrom"));
        Integer cityToId = Integer.parseInt(request.getParameter("cityTo"));
        Integer interval = Integer.parseInt(request.getParameter("interval"));
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) request.getSession().getAttribute(LIST_ORDER_ITEMS);

        mapService.saveOrUpdate(cityFromId, cityToId, interval);
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setCity(cityService.find(cityToId));
        orderItemEntity.setSequenceNumber(number);

        listOrderItems.add(orderItemEntity);
        return "redirect:/manager/orders/createPoint?number=" + number;
    }

    public String completeOrder(HttpServletRequest request) {
        Integer number = Integer.parseInt(request.getParameter("number"));
        HttpSession session = request.getSession();
        List<OrderItemEntity> listOrderItems = (List<OrderItemEntity>) session.getAttribute(LIST_ORDER_ITEMS);
        OrderEntity order = (OrderEntity) session.getAttribute(ORDER);
        List<CargoEntity> cargoList = (List<CargoEntity>) session.getAttribute(CARGO_LIST);

        ///if there are loading cargos
        if (!cargoList.isEmpty()) {
            return "redirect:/manager/orders/createPointCity?number=" + number + "&type=errorUnloadingCargos";
        }

        Integer duration = mapService.duration(listOrderItems);
        if (duration > MAX_HOURS) {
            return "redirect:/manager/orders/createPointCity?number=" + number + "&type=errorMaxHours";
        }
        order.setDuration(duration);
        order.setOrderItems(new HashSet<>(listOrderItems));

        return "redirect:/manager/orders/selectDrivers";
    }

    public String selectDriversAndSaveOrder(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes) {
        String[] driversIds = request.getParameterValues("drivers");
        OrderEntity order = (OrderEntity) request.getSession().getAttribute(ORDER);
        try {
            order = orderService.save(order, driversIds);
            clearSession(request);
            redirectAttributes
                    .addAttribute("id", order.getId())
                    .addAttribute("type", SUCCESS);
            return "redirect:/manager/orders/createResult";
        } catch (TruckAllreadyUsedException e) {
            logger.warn("Try to use truck in 2 orders.", e);
            clearSession(request);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Sorry, but your chosen truck is already busy!!! Please recreate the order.");
            redirectAttributes.addAttribute("type", ERROR);
            return "redirect:/manager/orders/createResult";
        } catch (DriverAllreadyUsedException e) {
            logger.warn("Try to use driver in 2 orders.", e);
            redirectAttributes.addAttribute("type", ERROR_DRIVER_USED);
            return "redirect:/manager/orders/selectDrivers";
        } catch (OrderIsInProgressException e) {
            logger.warn("Try to use driver in 2 orders.", e);
            redirectAttributes.addAttribute("type", ERROR);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Sorry, but you can't edit this order, it has started yet.");
            return "redirect:/manager/orders/createResult";
        }
    }

    public void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(ORDER);
        session.removeAttribute(MAX_WEIGHT);
        session.removeAttribute(LIST_ORDER_ITEMS);
        session.removeAttribute(CARGO_LIST);
        session.removeAttribute(RETURN_TO_SELECT_DRIVERS);
    }

    public String createDrivers(HttpServletRequest request) {
        request.getSession().setAttribute(RETURN_TO_SELECT_DRIVERS, true);
        return "redirect:/manager/newUsers";
    }

}
