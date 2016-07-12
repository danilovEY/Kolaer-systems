import org.springframework.web.servlet.view.InternalResourceViewResolver

beans {
    xmlns ([context:'http://www.springframework.org/schema/context'])
    context.'component-scan'('base-package':'ru.kolaer.server.webportal.controllers')

    xmlns mvc: "http://www.springframework.org/schema/mvc"

    viewResolver(InternalResourceViewResolver) {
        prefix = "/WEB-INF/views/"
        suffix = ".jsp"
    }
}