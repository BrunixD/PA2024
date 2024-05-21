package Phase2

class Anotations {
<<<<<<< Updated upstream

    @Target(AnnotationTarget.PROPERTY)
    annotation class Attribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class NestedContent

    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
    annotation class Directory(val name: String)


    @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
=======
    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlElementName(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlAttribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlExclude

    @Target(AnnotationTarget.CLASS)
    annotation class Directory(val name: String)

    @Target(AnnotationTarget.CLASS)
>>>>>>> Stashed changes
    annotation class Nested(val name: String)
}
