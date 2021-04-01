package utils.circebindings

/** Import all the contents of this object inside your controllers files in order to use
  * Circe Serialization for your objects.
  *
  * Unless you use Circe's annotation mechanism for your objects, you'll also need to import
  * the automatic implicit derivation, via
  * import io.circe.generic.auto._
  *
  * Import with
  * import utils.circebindings.ReadWriteImplicits._
  */
object ReadWriteImplicits extends ReadsImplicits with WriteableImplicits
