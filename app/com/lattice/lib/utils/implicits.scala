package com.lattice.lib.utils

/**
 * Created by Julien Déray on 22/10/2015.
 */
object Implicits {

  implicit class SeqImpl[T](seq:Seq[T]) {
    def sumBy[N](f:T=>N)(implicit num: Numeric[N])  = {
      seq.foldLeft(num.zero)((acc, b) => num.plus(acc, f(b)))
    }
  }
}
