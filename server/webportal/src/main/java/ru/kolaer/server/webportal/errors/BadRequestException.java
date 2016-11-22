package ru.kolaer.server.webportal.errors;

/**
 * Created by danilovey on 22.11.2016.
 */
public class BadRequestException extends RuntimeException
{
    public BadRequestException(String msg)
    {
        super(msg);
    }
}