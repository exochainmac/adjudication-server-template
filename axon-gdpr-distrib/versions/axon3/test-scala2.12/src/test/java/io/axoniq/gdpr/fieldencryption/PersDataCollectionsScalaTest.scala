/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption

import java.lang.invoke.MethodHandles
import java.util.concurrent.ThreadLocalRandom

import io.axoniq.gdpr.api.{FieldEncrypter, dataSubjectId, personalData}
import io.axoniq.gdpr.cryptoengine.{CryptoEngine, InMemoryCryptoEngine}
import io.axoniq.gdpr.utils.TestUtils.{isClear, isEncrypted}
import org.fluttercode.datafactory.impl.DataFactory
import org.junit.Assert.assertTrue
import org.junit.{Before, Test}
import org.slf4j.LoggerFactory

class PersDataCollectionsScalaTest {

  case class A(@dataSubjectId id: Int, @personalData names: scala.collection.immutable.Seq[String])
  case class B(@dataSubjectId id: Int, @personalData names: scala.collection.immutable.SortedSet[String])
  case class C(@dataSubjectId id: Int, @personalData names: scala.collection.mutable.Seq[_ <: String]) /* testing whether Scala wildcard upper bounds are accepted */
  case class D(@dataSubjectId id: Int, @personalData names: scala.collection.mutable.SortedSet[String])

  private val logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  private var cryptoEngine : CryptoEngine = _
  private var fieldEncrypter : FieldEncrypter = _
  private var dataFactory : DataFactory = _

  @Before
  def setUp(): Unit = {
    cryptoEngine = new InMemoryCryptoEngine
    fieldEncrypter = new FieldEncrypter(cryptoEngine)
    dataFactory = new DataFactory
    dataFactory.randomize(ThreadLocalRandom.current.nextInt)
  }

  @Test
  def personalDataCollFieldsMustGetEncryptedA(): Unit = {
    val a = A(3, dataFactory.getLastName :: dataFactory.getLastName :: dataFactory.getLastName :: Nil)
    logger.info("Before encryption: {}", a)
    fieldEncrypter.encrypt(a)
    logger.info("After encryption: {}", a)
    a.names.foreach((name: String) => assertTrue(isEncrypted(name)))
    fieldEncrypter.decrypt(a)
    a.names.foreach((name: String) => assertTrue(isClear(name)))
  }

  @Test
  def personalDataCollFieldsMustGetEncryptedB(): Unit = {
    val a = B(3, scala.collection.immutable.TreeSet(dataFactory.getLastName, dataFactory.getLastName, dataFactory.getLastName))
    logger.info("Before encryption: {}", a)
    fieldEncrypter.encrypt(a)
    logger.info("After encryption: {}", a)
    a.names.foreach((name: String) => assertTrue(isEncrypted(name)))
    fieldEncrypter.decrypt(a)
    a.names.foreach((name: String) => assertTrue(isClear(name)))
  }

  @Test
  def personalDataCollFieldsMustGetEncryptedC(): Unit = {
    val a = C(3, scala.collection.mutable.Seq(dataFactory.getLastName, dataFactory.getLastName, dataFactory.getLastName))
    logger.info("Before encryption: {}", a)
    fieldEncrypter.encrypt(a)
    logger.info("After encryption: {}", a)
    a.names.foreach((name: String) => assertTrue(isEncrypted(name)))
    fieldEncrypter.decrypt(a)
    a.names.foreach((name: String) => assertTrue(isClear(name)))
  }

  @Test
  def personalDataCollFieldsMustGetEncryptedD(): Unit = {
    val a = D(3, scala.collection.mutable.TreeSet(dataFactory.getLastName, dataFactory.getLastName, dataFactory.getLastName))
    logger.info("Before encryption: {}", a)
    fieldEncrypter.encrypt(a)
    logger.info("After encryption: {}", a)
    a.names.foreach((name: String) => assertTrue(isEncrypted(name)))
    fieldEncrypter.decrypt(a)
    a.names.foreach((name: String) => assertTrue(isClear(name)))
  }
}
