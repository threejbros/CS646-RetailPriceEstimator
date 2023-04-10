package com.zybooks.retailpriceestimator

class FinalPriceCalculator(itemListedPrice: Double, var sale_value: Double,
                           var clearance_value: Double, var military_first_responder_value: Double,
                           var tax_value: Double ) {
    var itemListedPrice = 0.00
        set(value) {
            field = if (value >= 0.00) value else 0.00
        }

    val finalPrice: String
        get() {
            val finalPriceDouble =  itemListedPrice * (1.0-sale_value) * (1.0-clearance_value) *
                    (1.0-military_first_responder_value) * (1.0 + tax_value)
            return String.format("%.2f", finalPriceDouble)
        }

    var defaultFinalPrice: String = "0.00"

    init {
        this.itemListedPrice = itemListedPrice
    }
}