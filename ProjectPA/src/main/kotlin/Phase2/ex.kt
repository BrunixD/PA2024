package phase2

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
        @Anotations.Nested("Nome")
        val nome: String,
        @Anotations.Nested("Ects")
        val ects: Double,
        @Anotations.Nested("Observações")
        val observacoes: String,

        )
@Anotations.Nested("Madje 1")
class NESTED(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,
    @Anotations.NestedContent()
    val conteudo: String,

    )

fun translate(obj: Any, lastParent : DirectoryEntity? = null) : Entity {
    if (obj::class.hasAnnotation<Anotations.Directory>()) {
        val name = obj::class.findAnnotation<Anotations.Directory>()?.name
        val newEntity = DirectoryEntity(name = name.toString(), lastParent)
        val members = obj::class.declaredMemberProperties
        members.forEach { member ->
            // Caso seja attribute adicionar a lista
            member.findAnnotation<Anotations.Attribute>()?.let {
                newEntity.attributes?.put(it.name, member.call(obj).toString())
            }
            // Caso seja Nested nao tem filhos
            member.findAnnotation<Anotations.Nested>()?.let {
                val nestedName = it.name
                val nestedContent = member.call(obj).toString()
                NestedEntity(nestedName, nestedContent, newEntity)
            }
            // Caso seja directory, aplicar recursiva

            member.findAnnotation<Anotations.Directory>()?.let {
                translate(it, newEntity)
            }
        }
        return newEntity
    } else{
        val name = obj::class.findAnnotation<Anotations.Nested>()?.name
        val newEntity = NestedEntity(name = name.toString());
        val members = obj::class.declaredMemberProperties
        members.forEach { member ->
            // Caso seja attribute adicionar a lista
            member.findAnnotation<Anotations.Attribute>()?.let {
                newEntity.attributes?.put(it.name, member.call(obj).toString())
            }
            // Caso tenha content
            member.findAnnotation<Anotations.NestedContent>()?.let {
                newEntity.content = member.call(obj).toString()
            }
        }
        return newEntity
    }
}

fun main(args: Array<String>) {
    val f = FUC("M4310", "Programação Avançada", 6.0, "la la...")
    val n = NESTED("atributo123" , "vou ser um conteudo")
    val rootDirectory = translate(f)
    val rootDirectory2 = translate(n)
    println(rootDirectory.prettyPrint(0))
    println("\n" + rootDirectory2.prettyPrint(0))
}
