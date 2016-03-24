package ca.uwaterloo.flix.language.ast

import scala.collection.immutable.Seq

trait WeededAst

object WeededAst {

  case class Program(roots: List[WeededAst.Root], hooks: Map[Symbol.Resolved, Ast.Hook], time: Time) extends WeededAst

  case class Root(decls: List[WeededAst.Declaration]) extends WeededAst

  /**
    * A common super-type for AST nodes that represents declarations.
    */
  sealed trait Declaration extends WeededAst {
    /**
      * Returns the source location of `this` declaration.
      */
    def loc: SourceLocation
  }

  object Declaration {

    /**
      * An AST node that represents a namespace declaration.
      *
      * @param name the the namespace.
      * @param body the nested declarations.
      * @param loc  the source location.
      */
    case class Namespace(name: Name.NName, body: List[WeededAst.Declaration], loc: SourceLocation) extends WeededAst.Declaration

    /**
      * An AST node that a fact declaration.
      *
      * @param head the head predicate.
      * @param loc  the source location.
      */
    case class Fact(head: WeededAst.Predicate.Head, loc: SourceLocation) extends WeededAst.Declaration

    /**
      * An AST node that represents a rule declaration.
      *
      * @param head the head predicate.
      * @param body the body predicate.
      * @param loc  the source location.
      */
    case class Rule(head: WeededAst.Predicate.Head, body: List[WeededAst.Predicate.Body], loc: SourceLocation) extends WeededAst.Declaration

  }

  /**
    * A common super-type for AST nodes that represents definitions.
    */
  sealed trait Definition extends WeededAst.Declaration

  object Definition {

    /**
      * An AST node that represent a constant definition.
      *
      * @param ident the name of the constant.
      * @param e     the named expression.
      * @param tpe   the declared type of the expression.
      * @param loc   the source location.
      */
    case class Constant(ident: Name.Ident, formals: List[WeededAst.FormalArg], e: WeededAst.Expression, tpe: Type, loc: SourceLocation) extends WeededAst.Definition

    /**
      * An AST node that represents an enum definition.
      *
      * @param ident the name of the enum.
      * @param cases the cases of the enum.
      * @param loc   the source location.
      */
    case class Enum(ident: Name.Ident, cases: Map[String, Type.UnresolvedTag], loc: SourceLocation) extends WeededAst.Definition

    /**
      * An AST node that represents a bounded lattice definition.
      *
      * @param tpe the type of the lattice elements.
      * @param bot the bot element.
      * @param top the top element.
      * @param leq the partial order.
      * @param lub the least upper bound.
      * @param glb the greatest lower bound.
      * @param loc the source location.
      */
    case class BoundedLattice(tpe: Type, bot: WeededAst.Expression, top: WeededAst.Expression, leq: WeededAst.Expression,
                              lub: WeededAst.Expression, glb: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Definition


    /**
      * An AST node that represents an index definition.
      *
      * @param ident   the name of the relation or lattice.
      * @param indexes the sequence of indexes.
      * @param loc     the source location.
      */
    case class Index(ident: Name.Ident, indexes: Seq[Seq[Name.Ident]], loc: SourceLocation) extends WeededAst.Definition

  }

  /**
    * A common super-type for tables that are either relations or lattices.
    */
  sealed trait Table extends WeededAst.Definition {
    /**
      * The name of `this` collection.
      */
    def ident: Name.Ident
  }

  object Table {

    /**
      * An AST node that represents a relation definition.
      *
      * @param ident      the name of the relation.
      * @param attributes the attributes of the relation.
      * @param loc        the source location of the relation.
      */
    case class Relation(ident: Name.Ident, attributes: List[WeededAst.Attribute], loc: SourceLocation) extends WeededAst.Table

    /**
      * An AST node that represents a lattice definition.
      *
      * @param ident the name of the lattice.
      * @param keys  the key attributes of the lattice.
      * @param value the value attributes of the lattice.
      * @param loc   the source location of the lattice.
      */
    case class Lattice(ident: Name.Ident, keys: List[WeededAst.Attribute], value: WeededAst.Attribute, loc: SourceLocation) extends WeededAst.Table

  }


  /**
    * A common super-type for AST node that represents literals.
    */
  sealed trait Literal extends WeededAst {
    /**
      * Returns the source location of `this` literal.
      */
    def loc: SourceLocation
  }

  object Literal {

    /**
      * An AST node that represents a unit literal
      *
      * @param loc the source location of the literal.
      */
    case class Unit(loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents a boolean literal.
      *
      * @param lit the boolean literal.
      * @param loc the source location of the literal.
      */
    case class Bool(lit: scala.Boolean, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents an char literal.
      *
      * @param lit the char literal.
      * @param loc the source location of the literal.
      */
    case class Char(lit: scala.Char, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents a float32 literal.
      *
      * @param lit the float32 literal.
      * @param loc the source location of the literal.
      */
    case class Float32(lit: scala.Float, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents a float64 literal.
      *
      * @param lit the float64 literal.
      * @param loc the source location of the literal.
      */
    case class Float64(lit: scala.Double, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents an int8 literal.
      *
      * @param lit the int8 literal.
      * @param loc the source location of the literal
      */
    case class Int8(lit: scala.Byte, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents an int16 literal.
      *
      * @param lit the int16 literal.
      * @param loc the source location of the literal
      */
    case class Int16(lit: scala.Short, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents an int32 literal.
      *
      * @param lit the int32 literal.
      * @param loc the source location of the literal
      */
    case class Int32(lit: scala.Int, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents an int64 literal.
      *
      * @param lit the int64 literal.
      * @param loc the source location of the literal
      */
    case class Int64(lit: scala.Long, loc: SourceLocation) extends WeededAst.Literal

    /**
      * An AST node that represents a string literal.
      *
      * @param lit the string literal.
      * @param loc the source location of the literal.
      */
    case class Str(lit: java.lang.String, loc: SourceLocation) extends WeededAst.Literal

  }

  /**
    * A common super-type for AST nodes representing expressions.
    */
  sealed trait Expression extends WeededAst {
    /**
      * Returns the source location of `this` expression.
      */
    def loc: SourceLocation
  }

  object Expression {

    /**
      * An AST node that represents a literal expressions.
      *
      * @param lit the literal.
      * @param loc the source location.
      */
    case class Lit(lit: WeededAst.Literal, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a variable or unresolved reference.
      *
      * @param name the unresolved name.
      * @param loc  the source location.
      */
    case class Var(name: Name.QName, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a lambda expressions.
      *
      * @param annotations the annotations.
      * @param formals     the formal arguments.
      * @param body        the body expression.
      * @param retTpe      the declared return type.
      * @param loc         the source location.
      */
    case class Lambda(annotations: Ast.Annotations, formals: List[WeededAst.FormalArg], body: WeededAst.Expression, retTpe: Type, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a call expression.
      *
      * @param lambda the lambda expression.
      * @param args   the argument expressions.
      * @param loc    the source location.
      */
    case class Apply(lambda: WeededAst.Expression, args: List[WeededAst.Expression], loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a unary expression.
      *
      * @param op  the unary operator.
      * @param e   the nested expression.
      * @param loc the source location.
      */
    case class Unary(op: UnaryOperator, e: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a binary expression.
      *
      * @param op  the binary operator.
      * @param e1  the lhs expression.
      * @param e2  the rhs expression.
      * @param loc the source location.
      */
    case class Binary(op: BinaryOperator, e1: WeededAst.Expression, e2: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents an if-then-else expression.
      *
      * @param e1  the conditional expression.
      * @param e2  the consequent expression.
      * @param e3  the alternate expression.
      * @param loc the source location.
      */
    case class IfThenElse(e1: WeededAst.Expression, e2: WeededAst.Expression, e3: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a switch expression.
      *
      * @param rules the rules of the switch.
      * @param loc   the source location.
      */
    case class Switch(rules: List[(WeededAst.Expression, WeededAst.Expression)], loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a let expression.
      *
      * @param ident the name of the bound variable.
      * @param value the value expression.
      * @param body  the body expression in which the let-bound variable occurs.
      * @param loc   the source location.
      */
    case class Let(ident: Name.Ident, value: WeededAst.Expression, body: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a match expression.
      *
      * @param e   the match value expression.
      * @param rs  the match rules.
      * @param loc the source location.
      */
    case class Match(e: WeededAst.Expression, rs: List[(WeededAst.Pattern, WeededAst.Expression)], loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a tagged expression.
      *
      * @param enum the enum name.
      * @param tag  the tag name.
      * @param e    the tagged expression.
      * @param loc  the source location.
      */
    case class Tag(enum: Name.QName, tag: Name.Ident, e: WeededAst.Expression, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a tuple expression.
      *
      * @param elms the elements of the tuple.
      * @param loc  the source location.
      */
    case class Tuple(elms: List[WeededAst.Expression], loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents a set expression.
      *
      * @param elms the elements of the set.
      * @param loc  the source location.
      */
    case class Set(elms: List[WeededAst.Expression], loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents an ascribe expression.
      *
      * @param e   the ascribed expression.
      * @param tpe the ascribed type.
      * @param loc the source location.
      */
    case class Ascribe(e: WeededAst.Expression, tpe: Type, loc: SourceLocation) extends WeededAst.Expression

    /**
      * An AST node that represents an error expression.
      *
      * @param tpe the type of the expression.
      * @param loc the source location.
      */
    case class Error(tpe: Type, loc: SourceLocation) extends WeededAst.Expression

  }

  /**
    * A common super-type for AST nodes that represent patterns.
    */
  sealed trait Pattern extends WeededAst {
    /**
      * Returns the set of free variables in `this` pattern.
      */
    def freeVars: Set[String] = this match {
      case WeededAst.Pattern.Wildcard(_) => Set.empty
      case WeededAst.Pattern.Var(ident, loc) => Set(ident.name)
      case WeededAst.Pattern.Lit(lit, loc) => Set.empty
      case WeededAst.Pattern.Tag(_name, ident, p, loc) => p.freeVars
      case WeededAst.Pattern.Tuple(elms, loc) => elms.foldLeft(Set.empty[String]) {
        case (acc, pat) => acc ++ pat.freeVars
      }
    }

    /**
      * The source location of `this` pattern.
      */
    def loc: SourceLocation
  }

  object Pattern {

    /**
      * An AST node that represents a wildcard pattern.
      *
      * @param loc the source location.
      */
    case class Wildcard(loc: SourceLocation) extends WeededAst.Pattern

    /**
      * An AST node that represents a variable pattern.
      *
      * @param ident the name of the variable.
      * @param loc   the source location.
      */
    case class Var(ident: Name.Ident, loc: SourceLocation) extends WeededAst.Pattern

    /**
      * An AST node that represents a literal pattern.
      *
      * @param lit the literal.
      * @param loc the source location.
      */
    case class Lit(lit: WeededAst.Literal, loc: SourceLocation) extends WeededAst.Pattern

    /**
      * An AST node that represents a tagged pattern.
      *
      * @param enum the enum name.
      * @param tag  the tag name.
      * @param pat  the nested pattern.
      * @param loc  the source location.
      */
    case class Tag(enum: Name.QName, tag: Name.Ident, pat: WeededAst.Pattern, loc: SourceLocation) extends WeededAst.Pattern

    /**
      * An AST node that represents a tuple pattern.
      *
      * @param elms the elements of the tuple.
      * @param loc  the source location.
      */
    case class Tuple(elms: scala.List[WeededAst.Pattern], loc: SourceLocation) extends WeededAst.Pattern

    case class List(hd: WeededAst.Pattern, tl: WeededAst.Pattern, loc: SourceLocation) extends WeededAst.Pattern

  }

  /**
    * A common super-type for AST nodes that represent predicates.
    */
  sealed trait Predicate extends WeededAst

  object Predicate {

    /**
      * A common super-type for AST nodes that represents head predicates.
      */
    sealed trait Head extends WeededAst.Predicate

    object Head {

      /**
        * An AST node that represents a relational predicate.
        *
        * @param name  the name of the relation.
        * @param terms the terms of the predicate.
        * @param loc   the source location.
        */
      case class Relation(name: Name.QName, terms: List[WeededAst.Term.Head], loc: SourceLocation) extends WeededAst.Predicate.Head

    }

    /**
      * A common super-type for AST nodes that represents body predicates.
      */
    sealed trait Body extends WeededAst.Predicate

    object Body {

      /**
        * An AST node that represent an ambiguous predicate.
        *
        * @param name  the name of the function or relation.
        * @param terms the terms of the predicate.
        * @param loc   the source location.
        */
      case class Ambiguous(name: Name.QName, terms: List[WeededAst.Term.Body], loc: SourceLocation) extends WeededAst.Predicate.Body

      /**
        * An AST node that represents the special not equal predicate.
        *
        * @param ident1 the name of the first variable.
        * @param ident2 the name of the second variable.
        * @param loc    the source location.
        */
      case class NotEqual(ident1: Name.Ident, ident2: Name.Ident, loc: SourceLocation) extends WeededAst.Predicate.Body

      /**
        * An AST node that represents the special loop predicate.
        *
        * @param ident the loop variable.
        * @param term  the set term.
        * @param loc   the source location.
        */
      case class Loop(ident: Name.Ident, term: WeededAst.Term.Head, loc: SourceLocation) extends WeededAst.Predicate.Body

    }

  }

  /**
    * A common super-type for AST nodes that represent terms.
    */
  sealed trait Term extends WeededAst {
    /**
      * The source location of `this` term.
      */
    def loc: SourceLocation
  }

  object Term {

    /**
      * A common super-type for AST nodes that represents head terms.
      */
    sealed trait Head extends WeededAst.Term

    object Head {

      /**
        * A variable term.
        *
        * @param ident the name of the variable.
        * @param loc   the source location.
        */
      case class Var(ident: Name.Ident, loc: SourceLocation) extends WeededAst.Term.Head

      /**
        * A literal term
        *
        * @param lit the literal.
        * @param loc the source location.
        */
      case class Lit(lit: WeededAst.Literal, loc: SourceLocation) extends WeededAst.Term.Head

      case class Tag(enumName: Name.QName, tagName: Name.Ident, t: WeededAst.Term.Head, loc: SourceLocation) extends WeededAst.Term.Head

      case class Tuple(elms: List[WeededAst.Term.Head], loc: SourceLocation) extends WeededAst.Term.Head

      /**
        * An apply term (function call).
        *
        * @param name the name of the function.
        * @param args the arguments to the function. 
        * @param loc  the source location.
        */
      case class Apply(name: Name.QName, args: List[WeededAst.Term.Head], loc: SourceLocation) extends WeededAst.Term.Head

    }

    /**
      * A common super-type for AST nodes that represents body terms.
      */
    sealed trait Body extends WeededAst.Term

    object Body {

      /**
        * A wildcard variable.
        *
        * @param loc the source location.
        */
      case class Wildcard(loc: SourceLocation) extends WeededAst.Term.Body

      /**
        * A variable term.
        *
        * @param ident the name of the variable.
        * @param loc   the source location.
        */
      case class Var(ident: Name.Ident, loc: SourceLocation) extends WeededAst.Term.Body

      /**
        * A literal term
        *
        * @param lit the literal.
        * @param loc the source location.
        */
      case class Lit(lit: WeededAst.Literal, loc: SourceLocation) extends WeededAst.Term.Body

      /**
        * An ascribe term.
        *
        * @param term the ascribed term.
        * @param tpe  the typed of the ascribed term.
        * @param loc  the source location.
        */
      case class Ascribe(term: Body, tpe: Type, loc: SourceLocation) extends WeededAst.Term.Body

    }

  }

  /**
    * An AST node that represents an attribute in a relation.
    *
    * @param ident the name of the attribute.
    * @param tpe   the declared type of the attribute.
    */
  case class Attribute(ident: Name.Ident, tpe: Type) extends WeededAst

  /**
    * An AST node representing a formal argument of a function.
    *
    * @param ident the name of the argument.
    * @param tpe   the type of the argument.
    */
  case class FormalArg(ident: Name.Ident, tpe: Type) extends WeededAst

}
