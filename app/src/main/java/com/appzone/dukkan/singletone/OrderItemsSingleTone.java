package com.appzone.dukkan.singletone;

import com.appzone.dukkan.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsSingleTone {

    private static OrderItemsSingleTone instance = null;
    private List<OrderItem> orderItemList = new ArrayList<>();

    private OrderItemsSingleTone(){}

    public static synchronized OrderItemsSingleTone newInstance()
    {
        if (instance == null)
        {
            instance = new OrderItemsSingleTone();
        }
        return instance;
    }

    public void AddProduct(OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);
        if (pos !=-1)
        {
            OrderItem item = orderItemList.get(pos);
            int product_new_quantity = orderItem.getProduct_quantity() + item.getProduct_quantity();
            item.setProduct_quantity(product_new_quantity);
            int product_total_price = item.getProduct_price() * orderItem.getProduct_quantity();
            item.setProduct_total_price(product_total_price);
            orderItemList.set(pos,item);

        }else
        {

            orderItemList.add(orderItem);

        }
    }

    public void UpdateProduct(OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);
        orderItemList.set(pos,orderItem);
    }

    public void RemoveProduct (OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);
        orderItemList.remove(pos);
    }

    private int getItemPosition(OrderItem orderItem)
    {
        int pos = -1;

        for (int i = 0 ; i< orderItemList.size() ; i++)
        {
            OrderItem item = orderItemList.get(i);
            if (item.getProduct_id().equals(orderItem.getProduct_id()))
            {
                pos = i;
                break;
            }
        }

        return pos;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public int getItemsCount ()
    {
        return orderItemList.size();
    }

    public void ClearCart()
    {
        orderItemList.clear();
    }

}