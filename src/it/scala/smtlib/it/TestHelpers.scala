package smtlib
package it

import scala.sys.process._

import org.scalatest.FunSuite

import java.io.File
import java.io.FileReader

import parser.Parser
import lexer.Lexer
import printer.RecursivePrinter
import interpreters._

/** Provide helper functions to test solver interfaces and files.
  *
  * Provides functions to access a list of files in resources.
  * Provides functions to get access to an interpreter to
  * an SMT solver running locally. Assume standard names of executable
  * are available in current working directory.
  */
trait TestHelpers {

  private val all: String => Boolean = (s: String) => true
  private val resourceDirHard = "src/it/resources/"

  def filesInResourceDir(dir : String, filter : String=>Boolean = all) : Iterable[File] = {    
    import scala.collection.JavaConversions._
    val d = this.getClass.getClassLoader.getResource(dir)
    val asFile = if(d == null || d.getProtocol != "file") {
      // We are in Eclipse. The only way we are saved is by hard-coding the path               
      new File(resourceDirHard + dir)
    } else new File(d.toURI())
    asFile.listFiles().filter(f => filter(f.getPath()))
  }
  
  def getZ3Interpreter: Interpreter = Z3Interpreter.buildDefault
  def getCVC4Interpreter: Interpreter = CVC4Interpreter.buildDefault

  def isZ3Available: Boolean = try {
    val output: String = "z3 -help".!! 
    true
  } catch {
    case (_: Exception) => false
  }
  
  def isCVC4Available: Boolean = try {
    val output: String = "cvc4".!!
    true
  } catch {
    case (e: Exception) => {
      false
    }
  }

  def executeZ3(file: File): Stream[String] = {
    val output: Stream[String] = Seq("z3", "-smt2", file.getPath).lineStream
    output
  }

  def executeCVC4(file: File): Stream[String] = {
    val output: Stream[String] = Seq("cvc4", "--lang", "smt", file.getPath).lineStream
    output
  }

//  def mkTest(file: File)(block: => Unit) = {
//
//    if(isZ3Available) {
//      test("SMTLIB benchmark: " + file.getPath) {
//        (new ScriptRunner).run(file)
//      }
//    }
//
//    if(isCVC4Available) {
//
//    }
//
//  }
//

}

