package main.kotlin

import kotlin.reflect.KClass

class Anotations {

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlAdapter(val adapter: KClass<out Adapter>)

    @Target(AnnotationTarget.PROPERTY)
    annotation class Attribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class WithPercentage()

    @Target(AnnotationTarget.PROPERTY)
    annotation class SelfClosingElementContent

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class ParentElement(val name: String)

    // When SelfClosingElement is a primitive property
    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class SelfClosingElementAttribute(val name: String)

    // When SelfClosingElement is a class property
    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class SelfClosingElement(val name: String)
}