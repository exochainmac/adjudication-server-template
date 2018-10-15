/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption

import java.util.UUID

import io.axoniq.gdpr.api.{dataSubjectId, personalData}

class BasicScalaTest extends AbstractBasicTestSet[DataRegisteredEvent] {
  override def createEvent(id: UUID, name: String, picture: Array[Byte], city: String): DataRegisteredEvent =
      DataRegisteredEvent(id, name, picture, city)
  override def getId(event: DataRegisteredEvent): UUID = event.id
  override def getName(event: DataRegisteredEvent): String = event.name
  override def getPicture(event: DataRegisteredEvent): Array[Byte] = event.picture
  override def getCity(event: DataRegisteredEvent): String = event.city
}

case class DataRegisteredEvent(
  @dataSubjectId id: UUID,
  @personalData name: String,
  @personalData picture: Array[Byte],
  city: String
) {
  override def equals(obj: Any): Boolean = obj match {
    case that: DataRegisteredEvent =>
      this.id == that.id &&
      this.name == that.name &&
      this.picture.toSeq == that.picture.toSeq &&
      this.city == that.city
    case _ => false
  }
}