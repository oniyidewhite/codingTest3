package com.oblessing.mobliepay.model

data class PageMeta(val offset: Int = 0, val totalRecords: Int = -1)
// -1 mean we don't currently know the total number of pages