package Phase2

import Phase2.Anotations
import main.DirectoryEntity
import main.Entity
import main.NestedEntity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Anotations.Directory("Fuc")
class FUC(
        @Anotations.Attribute("Código")
        val codigo: String,
        @Anotations.NestedAttribute("Nome")
        val nome: String,
        @Anotations.NestedAttribute("Ects")
        val ects: Double,
        @Anotations.NestedAttribute("Observações")
        val observacoes: String,
        @Anotations.Nested("NN")
        val nn: nn,

        )
@Anotations.Nested("NN")
class nn (
    @Anotations.Attribute("Atributo de NN")
    val NN : String,
        )
@Anotations.Nested("Madje 1")
class NESTED(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,
    @Anotations.NestedContent()
    val conteudo: String,

    )

fun translate(obj: Any, lastParent: DirectoryEntity? = null): Entity {
    val isDirectory = obj::class.hasAnnotation<Anotations.Directory>()
    val name = obj::class.findAnnotation<Anotations.Directory>()?.name ?: obj::class.findAnnotation<Anotations.Nested>()?.name
    val newEntity = if (isDirectory) DirectoryEntity(name = name.toString(), lastParent) else NestedEntity(name = name.toString())

    val members = obj::class.declaredMemberProperties
    members.forEach { member ->
        when {
            member.hasAnnotation<Anotations.Attribute>() -> {
                val attributeName = member.findAnnotation<Anotations.Attribute>()?.name ?: member.name
                val value = member.call(obj).toString() + if (member.hasAnnotation<Anotations.WithPercentage>()) "%" else ""
                newEntity.attributes?.put(attributeName, value)
            }
            member.hasAnnotation<Anotations.NestedAttribute>() -> {
                val nestedName = member.name
                val nestedContent = member.call(obj).toString()
                if (newEntity is DirectoryEntity) NestedEntity(nestedName, nestedContent, newEntity) else NestedEntity(nestedName, nestedContent)
            }
            member.hasAnnotation<Anotations.Nested>() -> {
                val nestedValue = member.call(obj)
                if (nestedValue != null) {
                    val nestedEntities = if (nestedValue is Collection<*>) nestedValue.map { translate(it!!, newEntity as DirectoryEntity) } else listOf(translate(nestedValue, newEntity as DirectoryEntity))
                    (newEntity as DirectoryEntity).children.addAll(nestedEntities)
                }
            }
            member.hasAnnotation<Anotations.Directory>() -> {
                val directoryValue = member.call(obj)
                if (directoryValue != null) {
                    val directoryEntities = if (directoryValue is Collection<*>) directoryValue.map { translate(it!!, newEntity as DirectoryEntity) } else listOf(translate(directoryValue, newEntity as DirectoryEntity))
                    //(newEntity as DirectoryEntity).children.addAll(directoryEntities)
                }
            }
        }
    }
    return newEntity
}


fun main(args: Array<String>) {
    val nn = nn("ola")
    val f = FUC("M4310", "Programação Avançada", 6.0, "la la...", nn)
    val n = NESTED("atributo123" , "vou ser um conteudo")
    val rootDirectory = translate(f)
    val rootDirectory2 = translate(n)
    println(rootDirectory.prettyPrint(0))
    println("\n" + rootDirectory2.prettyPrint(0))
}
