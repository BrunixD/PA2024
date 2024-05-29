package main.kotlin

import kotlin.reflect.KClass


/**
 * Contains custom annotations used to define and transform XML elements and attributes.
 */
class Anotations {

    /**
     * Annotation to specify an adapter class for an XML element.
     *
     * @property adapter The adapter class to use.
     */
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
    annotation class XmlAdapter(val adapter: KClass<out Adapter>)

    /**
     * Annotation to mark a property as an attribute in an XML element.
     *
     * @property name The name of the attribute in the XML.
     */
    @Target(AnnotationTarget.PROPERTY)
    annotation class Attribute(val name: String)

    /**
     * Annotation to indicate that a property's value should be suffixed with a percentage sign (%).
     */
    @Target(AnnotationTarget.PROPERTY)
    annotation class WithPercentage()

    /**
     * Annotation to mark a property as the content of a self-closing XML element.
     */
    @Target(AnnotationTarget.PROPERTY)
    annotation class SelfClosingElementContent

    /**
     * Annotation to mark a class or property as a parent element in the XML structure.
     *
     * @property name The name of the parent element in the XML.
     */
    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class ParentElement(val name: String)

    /**
     * Annotation to mark a property (primitive string) as a self-closing element attribute in the XML structure.
     *
     * @property name The name of the self-closing element attribute in the XML.
     */
    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class SelfClosingElementPrimitive(val name: String)

    /**
     * Annotation to mark a property (class type) as a self-closing element attribute in the XML structure.
     *
     * @property name The name of the self-closing element attribute in the XML.
     */
    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class SelfClosingElement(val name: String)
}