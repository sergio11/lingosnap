package com.dreamsoftware.lingosnap.domain.validation

interface IBusinessEntityValidator<T> {
    fun validate(entity: T): Map<String, String>
}