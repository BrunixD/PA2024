package tests
import main.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test



val plano = DirectoryEntity("plano")
val cursoPA = NestedEntity("curso", content = "Mestrado em Engenharia Informática", parent = plano)
val fucPA = DirectoryEntity("fuc", parent = plano)
val nomePA = NestedEntity("nome", parent = fucPA, content = "Programação Avançada")
val ects = NestedEntity("ects", parent = fucPA, content = "6.0")
val avaliacao = DirectoryEntity("avaliacao", parent = fucPA)
val componenteQ = NestedEntity("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Quizzes\"", "peso=\"20%\"")*/)
val componenteP = NestedEntity("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Projeto\"", "peso=\"80%\"")*/)
val fuc2 = DirectoryEntity("fuc2", parent=plano, /*attributes =mutableMapOf("codigo='03782'")*/)
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



}

