package com.task.shopmarket.utils;

import com.task.shopmarket.models.Carrito;

import java.util.List;

public interface EscuchaCarrito {
    void onCarritoChange(List<Carrito> carrito);
}
