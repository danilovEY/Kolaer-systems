# Система "КолАЭР"

## О системе

Система разработана для организации "[КолАтомэнергоремонт](http://kolaer.ru/)". Включает в себя структуру приложений, состоящее из:

* **API:** Содержит интерфейсы для создания плагинов, служб, взаимодействия с основным клиентским приложением, а также с серверами.
* **Client**:
 * **Uniform System Application (USA)** - Единая оболочка клиентского приложения. Основные особенности:
  1. Расширяемость с помощью плагинов
  2. Реализация и исполнение системных и кастомных служб
  3. Реализация API в виде Editor Kit'а
  4. Реализация автоматического сканера плагинов
  5. Минимизация приложения
 * **Плагин "АСУП"** - CRUD для файлов и сторонних приложений.
 * **Плагин "Birthday"** - Уведомляет сотрудников о днях рождения
 * **Плагин "Admin Panel"** - Панель с разными инструментами для администраторов
 * **Плагин "Browser"** - Реализация браузера на основе [Jxbrowser](https://www.teamdev.com/jxbrowser).
* **Server (GlassFish/Tomcat):**
 * **RESTful** - RESTful сервер для работы и взаимодействия с клиентским приложение и БД через json объекты
 * **Kolaer-web (Alpha)** - Различного рода веб приложения

## Стек технологий

* **Uniform System Application (USA)** - *OSGi (Apache Felix)*,  *Jackson*, *Jersey (client)*, *JavaFX*, *ControlFX*
* **RESTful** - *Spting-MVC (rest)*, *Hibernate (JPA)*
* **Kolaer-web** - *Spting-MVC(rest/jsp)*, *Spting-Security*, *Hibernate*, *AngularJS*, *HTML5*, *CSS3*
* **Build** - *Gradle (Multiproject)*, *Maven (Standalone)*