10
https://raw.githubusercontent.com/GuntherRademacher/rr/master/src/main/java/de/bottlecaps/webapp/servlet/ServletCookie.java
package de.bottlecaps.webapp.servlet;

import de.bottlecaps.webapp.Cookie;

public class ServletCookie implements Cookie
{
  private javax.servlet.http.Cookie cookie;

  public ServletCookie(javax.servlet.http.Cookie cookie)
  {
    this.cookie = cookie;
  }

  @Override
  public String getName()
  {
    return cookie.getName();
  }

  @Override
  public String getValue()
  {
    return cookie.getValue();
  }
}
