package Phase2

class Anotations {

    @Target(AnnotationTarget.PROPERTY)
    annotation class Attribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class NestedAttribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class DirectoryAttribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class NestedContent

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class Directory(val name: String)


    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class Nested(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class WithPercentage()

}
