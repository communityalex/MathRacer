/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-10-17 16:43:55 UTC)
 * on 2016-10-23 at 09:04:03 UTC 
 * Modify at your own risk.
 */

package com.example.alex.mathracer.backend.mathRacerApi.model;

/**
 * Model definition for Student.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the mathRacerApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Student extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String firstName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String identifier;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastRaceTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Teacher teacher;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName firstName or {@code null} for none
   */
  public Student setFirstName(java.lang.String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Student setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getIdentifier() {
    return identifier;
  }

  /**
   * @param identifier identifier or {@code null} for none
   */
  public Student setIdentifier(java.lang.String identifier) {
    this.identifier = identifier;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastName() {
    return lastName;
  }

  /**
   * @param lastName lastName or {@code null} for none
   */
  public Student setLastName(java.lang.String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastRaceTime() {
    return lastRaceTime;
  }

  /**
   * @param lastRaceTime lastRaceTime or {@code null} for none
   */
  public Student setLastRaceTime(java.lang.String lastRaceTime) {
    this.lastRaceTime = lastRaceTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Teacher getTeacher() {
    return teacher;
  }

  /**
   * @param teacher teacher or {@code null} for none
   */
  public Student setTeacher(Teacher teacher) {
    this.teacher = teacher;
    return this;
  }

  @Override
  public Student set(String fieldName, Object value) {
    return (Student) super.set(fieldName, value);
  }

  @Override
  public Student clone() {
    return (Student) super.clone();
  }

}