package fix

import scalafix._
import scalafix.syntax._
import scala.meta._

object Util {
  def rename(ctx: RewriteCtx, t: Term.Name, renames: Map[String, String])(
      implicit mirror: SemanticCtx
  ): Patch = {
    renames.collect {
      case (target, rename) if t.isSymbol(target) =>
        ctx.replaceTree(t, rename)
    }.asPatch
  }

  implicit class TermNameOps(t: Name) {
    def isSymbol(s: String)(implicit mirror: SemanticCtx): Boolean = {
      t.symbolOpt.exists(_.normalized.syntax == s)
    }

    def isOneOfSymbols(symbols: Set[String])(implicit mirror: SemanticCtx): Boolean =
      t.symbolOpt.exists(s => symbols.contains(s.normalized.syntax))
  }
}

case class Scalafixargonautcirce_v0_0_1(mirror: SemanticCtx)
    extends SemanticRewrite(mirror) {
  import Util._

  val renames: Map[String, String] =
    (1 to 22).flatMap { arity =>
      Seq(
        s"_root_.argonaut.GeneratedDecodeJsons.jdecode${arity}L." -> s"Decoder.forProduct$arity"
      )
    }.toMap

  def rewrite(ctx: RewriteCtx): Patch = {
    ctx.reporter.info(ctx.tree.syntax)
    ctx.reporter.info(ctx.tree.structure)

    ctx.tree.collect {
      case i @ Importer(Term.Name("argonaut"), List(Importee.Wildcard())) =>
        ctx.replaceTree(i, "io.circe._, io.circe.parser._")
      case i @ Importer(Term.Name("Argonaut"), List(Importee.Wildcard())) =>
        ctx.replaceTree(i, "io.circe.syntax._")
      case defnVal @ Defn.Val(
            mods,
            listName, // List(Pat.Var.Term(Term.Name(userDecodeJson))),
            typeDefinition,
            Term.Apply(
              Term.Apply(n @ Term.Name(jdecode3L),
                         List(Term.Select(Term.Name(user), Term.Name(apply)))),
              List(Lit.String(id),
                   Lit.String(first_name),
                   Lit.String(last_name))
            )
          ) if n.isOneOfSymbols(renames.keySet) =>
        val renameTo = renames(n.symbol.get.normalized.syntax)
        val renameTerm = Term.Name.apply(renameTo)
        val typeTerm = Term.Name.apply(user)

        val q = typeDefinition match {
          case Some(
              Type.Apply(Type.Name(decodeJson), List(tVar @ Type.Name(tName)))
              ) =>
            q"""val ${listName.head}: Decoder[${tVar}] = $renameTerm("id", "first_name", "last_name")($typeTerm.apply)"""
          case _ =>
            q"""val ${listName.head} = $renameTerm("id", "first_name", "last_name")($typeTerm.apply)"""
        }

        val qq = q.copy(mods = mods)
        ctx.replaceTree(defnVal, qq.toString())
      case defnDef @ Defn.Def(
            mods,
            name,
            tparams @ List(),
            paramss @ List(),
            decltpe,
            body @ Term.Apply(
              Term.Apply(
                n @ Term.Name(jdecode3L),
                List(
                  Term.Select(typeTerm @ Term.Name(user), Term.Name("apply"))
                )
              ),
              fields
            )
          ) if n.isOneOfSymbols(renames.keySet) =>
        val renameTo = renames(n.symbol.get.normalized.syntax)
        val renameTerm = Term.Name.apply(renameTo)
        val dMod = defnDef.copy(
          decltpe = defnDef.decltpe match {
            case Some(
                Type.Apply(Type.Name("DecodeJson"), List(typeTerm))
                ) =>
              Some(
                Type.Apply(Type.Name("Decoder"), List(typeTerm))
              )
            case _ => None
          },
          body = Term.Apply(
            Term.Apply(renameTerm, fields),
            List(Term.Select(typeTerm, Term.Name("apply")))
          )
        )
        ctx.replaceTree(defnDef, dMod.toString())
    }.asPatch
  }
}
