package com.oblessing.mobliepay.core

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntity(entity: EntityModel): DomainModel
}