package svcs

import java.io.File
import java.security.MessageDigest
import java.util.*

const val VCS_DIR = "vcs"
const val COMMIT_DIR = "$VCS_DIR/commits"
var name = ""
var filesList = mutableListOf<String>()
var lastCommit = ""
val configFile = File("$VCS_DIR/config.txt")
val indexFile = File("$VCS_DIR/index.txt")
val logFile = File("$VCS_DIR/log.txt")

fun main(args: Array<String>) {
    val command = if (args.isNotEmpty()) args[0] else ""
    val parameters = if (args.size > 1) args[1] else ""

    File(COMMIT_DIR).mkdirs()
    if (!configFile.exists()) configFile.createNewFile()
    else name = configFile.readText()

    if (!indexFile.exists()) indexFile.createNewFile()
    else indexFile.forEachLine { filesList.add(it) }

    if (!logFile.exists()) logFile.createNewFile()
    if (logFile.length() > 0) lastCommit = logFile.readLines().first().split(" ")[1]

    when (command) {
        "", "--help" -> printHelp()
        "config" -> configCommand(parameters)
        "add" -> addCommand(parameters)
        "log" -> printLog()
        "commit" -> commitCommand(parameters)
        "checkout" -> checkoutCommand(parameters)
        else -> printWrong(args[0])
    }
}

fun printHelp() {
    println("""
These are SVCS commands:
config     Get and set a username.
add        Add a file to the index.
log        Show commit logs.
commit     Save changes.
checkout   Restore a file.
""")
}

fun configCommand(parameters: String) {
    if (parameters.isNotEmpty()) name = parameters
    if (name.isEmpty()) println("Please, tell me who you are.")
    else {
        println("The username is $name.")
        configFile.writeText(name)
    }
}

fun addCommand(parameters: String) {
    if (filesList.isEmpty() && parameters.isEmpty()) {
        println("Add a file to the index.")
    } else {
        if (parameters.isNotEmpty()) {
            if (File(parameters).exists()) {
                filesList.add(parameters)
                println("The File '$parameters' is tracked.")
                indexFile.appendText("$parameters\n")
            } else {
                println("Can't found '$parameters'.")
            }
        } else {
            println("Tracked files:")
            filesList.forEach {
                println(it)
            }
        }
    }
}

fun printLog() {
    val lines = logFile.readText()
    if (lines.isEmpty()) println("No commits yet.")
    else {
        logFile.forEachLine {
            println(it)
        }
    }
}

fun commitCommand(parameters: String) {
    if (parameters.isEmpty()) {
        println("Message was not passed.")
        return
    }

    val changeList = mutableListOf<String>()
    filesList.forEach {
        var digest1 = ""
        var digest2 = ""
        if (File(it).exists()) digest1 = File(it).readText().sha256()
        val path = "$COMMIT_DIR/$lastCommit/$it"
        if (File(path).exists()) digest2 = File(path).readText().sha256()
        if (digest1 != digest2) changeList.add(it)
    }

    if (changeList.isEmpty()) println("Nothing to commit.")
    else {
        println("Changes are committed.")
        val uniqueID = UUID.randomUUID().toString()
        val path = File("$COMMIT_DIR/$uniqueID")
        for (file in changeList) {
            File(file).copyTo(File("$path/$file"))
        }
        val old = logFile.readText()
        logFile.writeText("commit $uniqueID\n")
        logFile.appendText("Author: $name\n")
        logFile.appendText("$parameters\n\n")
        logFile.appendText(old)
    }
}

fun checkoutCommand(parameters: String) {
    if (parameters.isEmpty()) {
        println("Commit id was not passed.")
        return
    }

    if (logFile.readText().contains("commit $parameters")) {
        val path = File("$COMMIT_DIR/$parameters")
        path.list()!!.forEach { File("$COMMIT_DIR/$parameters/$it").copyTo(File(it), true) }
        println("Switched to commit $parameters.")
    }
    else
        println("Commit does not exist.")
}

fun printWrong(command: String) {
    println("'$command' is not a SVCS command.")
}

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}
