package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class OrderService {

    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();
    private final LongAdder nextId = new LongAdder();

    private long nextId() {
        return nextId.sum();
    }

    public long createOrder(List<Item> items) {

        long id = nextId();

        currentOrders.compute(id, (aLong, order1) -> {
            Order order = new Order(items);
            order.setId(id);
            return order;
        });

        return id;
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {

        currentOrders.computeIfPresent(orderId, (aLong, order) -> {
            order.setPaymentInfo(paymentInfo);
            addDeliver(order);
            return order;
        });

//        if (currentOrders.get(orderId).checkStatus()) {
//            deliver(currentOrders.get(orderId));
//        }
    }


    public void setPacked(long orderId) {

        currentOrders.computeIfPresent(orderId, (aLong, order) -> {
            order.setPacked(true);
            addDeliver(order);
            return order;
        });
    }

    private synchronized void deliver(Order order) {
        /* ... */
        currentOrders.get(order.getId()).setStatus(Order.Status.DELIVERED);
//        currentOrders.compute(order.getId(), (aLong, order1) -> order1.setStatus(Order.Status.DELIVERED))
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
    private void addDeliver(Order order) {
        if (order.checkStatus()) deliver(order);
    }
}
