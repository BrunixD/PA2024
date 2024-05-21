package `Phase 2`

class Anotations {
    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlElementName(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlAttribute(val name: String)

    @Target(AnnotationTarget.PROPERTY)
    annotation class XmlExclude

    @Target(AnnotationTarget.CLASS)
    annotation class Directory(val name: String)

    @Target(AnnotationTarget.CLASS)
    annotation class Nested(val name: String)
}
