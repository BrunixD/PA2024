package Phase2

import main.kotlin.Phase2.Adapter
import kotlin.reflect.KClass

class Anotations {

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlAdapter(val adapter: KClass<out Adapter>)

    @Target(AnnotationTarget.PROPERTY)
    annotation class Attribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class WithPercentage()

    @Target(AnnotationTarget.PROPERTY)
    annotation class NestedContent

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class Directory(val name: String)

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class NestedAttribute(val name: String)

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class Nested(val name: String)
}