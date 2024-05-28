package tests
import Phase2.Anotations
import main.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import Phase2.*
import main.kotlin.Phase2.translate

val plano = DirectoryEntity("plano")
val cursoPA = NestedEntity("curso", content = "Mestrado em Engenharia Informática", parent = plano)
val fucPA = DirectoryEntity("fuc", parent = plano)
val nomePA = NestedEntity("nome", parent = fucPA, content = "Programação Avançada")
val ects = NestedEntity("ects", parent = fucPA, content = "6.0")
val avaliacao = DirectoryEntity("avaliacao", parent = fucPA)
val componenteQ = NestedEntity("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Quizzes\"", "peso=\"20%\"")*/)
val componenteP = NestedEntity("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Projeto\"", "peso=\"80%\"")*/)
val fuc2 = DirectoryEntity("fuc2", parent=plano, /*attributes =mutableMapOf("codigo='03782'")*/)

@Anotations.Directory("DWithN")
class DirectorywithNest(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.NestedAttribute("Nome")
    val nome: String,
    @Anotations.NestedAttribute("Ects")
    val ects: Double,
    @Anotations.NestedAttribute("Observações")
    val observacoes: String,

    )
@Anotations.Directory("DWtihoutN")
class DirectorywithoutNest(
    @Anotations.Attribute("Código")
    val codigo: String,
    )

@Anotations.Nested("NestedWithContent")
class NestedWithContent(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,
    @Anotations.NestedContent()
    val conteudo: String,

    )

@Anotations.Nested("NestedWithoutContent")
class NestedWithoutContent(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,

    )

@Anotations.Directory("DWithListOfChildren")
class DirectorywithListOfChildren(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.NestedAttribute("Nome")
    val nome: String,
    @Anotations.NestedAttribute("Ects")
    val ects: Double,
    @Anotations.NestedAttribute("Observações")
    val observacoes: String,
    @Anotations.Nested("Avaliacao")
    val avaliacao: List<NestedWithoutContent>
    )

@Anotations.Directory("DWithListOfChildrenDirectory")
class DirectorywithListOfChildrenDirectory(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.NestedAttribute("Nome")
    val nome: String,
    @Anotations.NestedAttribute("Ects")
    val ects: Double,
    @Anotations.NestedAttribute("Observações")
    val observacoes: String,
    @Anotations.Directory("Avaliacao")
    val avaliacao: List<DirectorywithNest>
)

class tests{

    @Test
    fun removeEntityTest() {
        cursoPA.removeEntity()
        assertFalse(plano.children.contains(cursoPA))
    }

    /**
     * Tests the addition of a nested entity to a directory entity.
     */
    @Test
    fun addNestedEntityTest() {
        val testEntity = NestedEntity("testEntity")
        plano.addNestedEntity("testEntity")
        assertFalse(plano.children.contains(testEntity))
    }

    /**
     * Tests the addition of a directory entity to another directory entity.
     */
    @Test
    fun addDirectoryEntityTest() {
        val testEntity = NestedEntity("testEntity")
        plano.addDirectoryEntity("testEntity")
        assertFalse(plano.children.contains(testEntity))
    }

    /**
     * Tests the addition of an attribute to an entity.
     */
    @Test
    fun addAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("content", "2020")
        val testEntity = DirectoryEntity("testEntity")
        testEntity.addAttribute("content", "2020")
        assertEquals(attributes, testEntity.attributes)
    }

    /**
     * Tests the removal of an attribute from an entity.
     */
    @Test
    fun removeAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("content", "2020")
        val testEntity = DirectoryEntity("testEntity")
        testEntity.addAttribute("content", "2020")
        testEntity.removeAttribute("content")
        assertTrue(testEntity.attributes!!.isEmpty())
    }

    /**
     * Tests the alteration of an attribute value in an entity.
     */
    @Test
    fun alterAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes["content"] = "2020"
        val attributes2: MutableMap<String, String> = mutableMapOf()
        attributes2["content"] = "2000"
        val subCurso = DirectoryEntity("aaa")
        subCurso.addAttribute("content", "2020")
        subCurso.alterAttribute("content", "2000")
        assertEquals(attributes2, subCurso.attributes)
    }

    /**
     * Tests the pretty-printing of a directory entity.
     */
    @Test
    fun prettyPrintTest() {
        componenteQ.addAttribute("nome", "Quizz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        cursoPA.addAttribute("content", "2020")
        plano.addNestedEntity(entityName = "teste", entityContent = "Isto e content")
        assertEquals(
            "<plano>\n" +
                    "\t<curso content=\"2020\">Mestrado em Engenharia Informática</curso>\n" +
                    "\t<fuc>\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente nome=\"Quizz\" Peso=\"20%\"/>\n" +
                    "\t\t\t<componente nome=\"Projeto\" Peso=\"80%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc2/>\n" +
                    "\t<teste>Isto e content</teste>\n" +
                    "</plano>\n", plano.prettyPrint()
        )
    }

    /**
     * Tests the addition of a global attribute to entities with the specified name.
     */
    @Test
    fun addAttributeGlobalTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("global", "2020")
        plano.addAttributeGlobal("curso", "global", "2020")
        assertEquals(attributes, cursoPA.attributes)
    }

    /**
     * Tests the renaming of entities with the specified old name to the new name.
     */
    @Test
    fun renameEntityGlobalTest() {
        plano.renameEntityGlobal("curso", "UC")
        assertEquals("UC", cursoPA.name)
    }

    /**
     * Tests the renaming of attributes across entities with the specified name.
     */
    @Test
    fun renameAttributeNameGlobalTest() {
        componenteQ.addAttribute("nome", "Quizz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        plano.renameAttributeNameGlobal("componente", "nome", "Avaliacao")

        assertTrue(componenteQ.attributes?.get("Avaliacao") == "Quizz")
        assertTrue(componenteP.attributes?.get("Avaliacao") == "Projeto")
    }

    /**
     * Tests the removal of entities with the specified name from the hierarchy.
     */
    @Test
    fun removeEntityGlobalTest() {
        componenteQ.addAttribute("nome", "Quiz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        plano.removeEntityGlobal("componente")

        assertFalse(avaliacao.children.contains(componenteQ))
        assertFalse(avaliacao.children.contains(componenteP))
    }

    /**
     * Tests the removal of attributes from entities with the specified name.
     */
    @Test
    fun removeAttributeGlobalTest() {
        componenteQ.addAttribute("nome", "Quiz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        val componenteX = NestedEntity("componente", parent = fucPA)
        componenteX.addAttribute("nome", "Kotlin knowledge")
        componenteX.addAttribute("Peso", "0%")

        plano.removeAttributeGlobal("componente", "Peso")

        assertNull(componenteQ.attributes?.get("Peso"))
        assertNull(componenteP.attributes?.get("Peso"))
        assertNull(componenteX.attributes?.get("Peso"))
    }

    /**
     * Tests the XPath-like search across the hierarchy.
     */
    @Test
    fun xPathTest() {
        assertEquals("<componente/>\n" + "<componente/>\n", plano.xPath("plano/fuc/avaliacao/componente"))
        assertEquals(avaliacao.prettyPrint(), plano.xPath("plano/fuc/avaliacao"))
    }

    @Test
    fun testTranslateNestedEntity() {


        // Act
        val DWN = DirectorywithNest("M4A1", "Diretório com Nest", 6.0, "Desgosto")
       // val DWON = DirectorywithoutNest("M4A1")
       // val NWC = NestedWithContent("atributo_1", "Conteúdo de um Nested")
       // val NWOC = NestedWithoutContent("atributo_2")
        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val DirectoryTest = DirectoryEntity("DWithN", null, attributes)
        print(translate(DWN).prettyPrint())
        assertEquals(DirectoryTest, translate(DWN))
    }

    @Test
    fun testTranslateDirectoryWithoutNestedEntities() {
        // Arrange
        val directoryObject = object {
            @Anotations.Directory("TestDir")
            @Anotations.Attribute("TestAttr")
            val attr: String = "AttrValue"
        }

        // Act
        val result = translate(directoryObject)

        // Assert
        assertTrue(result is DirectoryEntity)
        val directoryEntity = result as DirectoryEntity
        assertEquals("TestDir", directoryEntity.name)
        assertNotNull(directoryEntity.attributes)
        assertEquals("AttrValue", directoryEntity.attributes?.get("TestAttr"))
        assertTrue(directoryEntity.children.isEmpty())
    }

    @Test
    fun testTranslateDirectoryWithNestedEntities() {
        // Arrange
        val directoryObject = object {
            @Anotations.Directory("TestDir")
            @Anotations.Attribute("TestAttr")
            val attr: String = "AttrValue"
            @Anotations.Nested("TestNested")
            val nested: String = "NestedValue"
        }

        // Act
        val result = translate(directoryObject)

        // Assert
        assertTrue(result is DirectoryEntity)
        val directoryEntity = result as DirectoryEntity
        assertEquals("TestDir", directoryEntity.name)
        assertNotNull(directoryEntity.attributes)
        assertEquals("AttrValue", directoryEntity.attributes?.get("TestAttr"))

        val nestedEntities = directoryEntity.children.filterIsInstance<NestedEntity>()
        assertEquals(1, nestedEntities.size)

        val nestedEntity = nestedEntities.find { it.name == "TestNested" }
        assertNotNull(nestedEntity)
        assertEquals("NestedValue", nestedEntity?.content)
    }

    @Test
    fun testTranslateDirectoryWithDirectoryChildren() {
        // Arrange
        val parentObject = object {
            @Anotations.Directory("ParentDir")
            val child = object {
                @Anotations.Directory("ChildDir")
                @Anotations.Attribute("ChildAttr")
                val attr: String = "ChildAttrValue"
            }
        }

        // Act
        val result = translate(parentObject)

        // Assert
        assertTrue(result is DirectoryEntity)
        val parentEntity = result as DirectoryEntity
        assertEquals("ParentDir", parentEntity.name)

        val childDirectories = parentEntity.children.filterIsInstance<DirectoryEntity>()
        assertEquals(1, childDirectories.size)

        val childEntity = childDirectories.find { it.name == "ChildDir" }
        assertNotNull(childEntity)
        assertNotNull(childEntity?.attributes)
        assertEquals("ChildAttrValue", childEntity?.attributes?.get("ChildAttr"))
    }

    @Test
    fun testTranslateDirectoryWithListOfChildrenNested() {
        // Act
        val NWC = NestedWithoutContent("atributo_1")
        val NWC2 = NestedWithoutContent("atributo_2")
        val list = listOf<NestedWithoutContent>(NWC,NWC2)
        val DWLC = DirectorywithListOfChildren("M4A1", "Diretório com Nest", 6.0, "Desgosto",list)

        // val DWON = DirectorywithoutNest("M4A1")

        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(DWLC).prettyPrint())
        val DirectoryTest = DirectoryEntity("DWithListOfChildren", null, attributes)
        assertEquals(DirectoryTest, translate(DWLC))
    }

    @Test
    fun testTranslateDirectoryWithListOfChildrenDirectory() {
        // Act
        val NWC = DirectorywithNest("M4A1", "Diretório com Nest 1", 4.0, "Desgosto 1")
        val NWC2 = DirectorywithNest("M4A2", "Diretório com Nest 2", 5.0, "Desgosto 2")
        val list = listOf<DirectorywithNest>(NWC,NWC2)
        val DWLC = DirectorywithListOfChildrenDirectory("M4A1", "Diretório com Nest", 6.0, "Desgosto",list)

        // val DWON = DirectorywithoutNest("M4A1")

        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(DWLC).prettyPrint())
        val DirectoryTest = DirectoryEntity("DWithListOfChildrenDirectory", null, attributes)
        assertEquals(DirectoryTest, translate(DWLC))
    }
}

