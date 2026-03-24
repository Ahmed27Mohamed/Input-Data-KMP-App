package com.a2004256_ahmedmohamed.inputdata

class Customer() {
    var name: String = ""
    var phone: String = ""
    var address: String = ""
    var birthDate: String = ""
    var points: Int = 0
    var invoiceAmount: Int = 0
    var invoiceDescription: String = ""

    constructor(
        name: String,
        phone: String,
        address: String,
        birthDate: String,
        points: Int,
        invoiceAmount: Int,
        invoiceDescription: String
    ) : this() {
        this.name = name
        this.phone = phone
        this.address = address
        this.birthDate = birthDate
        this.points = points
        this.invoiceAmount = invoiceAmount
        this.invoiceDescription = invoiceDescription
    }
}