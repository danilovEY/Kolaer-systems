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
  5. Запуск только одной копии приложения
 * **Плагин "АСУП"** - "Декстоп" организации
 * **Плагин "Birthday"** - Календарь сотрудников 
 * **Плагин "Admin Panel"** - Панель управления для администраторов
 * **Плагин "Kolpass"** - Парольница
 * **Плагин "PSR"** - Реестр ПСР-проектов
 * **Плагин "JAPC"** - Реестр журналов нарушений
 * **Плагин "Browser"** - Реализация браузера на основе [Jxbrowser](https://www.teamdev.com/jxbrowser).
* **Server (~~GlassFish/Tomcat~~ Jetty):**
 * **RESTful** - Сервер для телеметрии
 * **Kolaer-web** - RESTful сервер приложение и инструментов

## Стек технологий

* **Uniform System Application (USA)** - *OSGi (Apache Felix)*,  *Jackson*, *SpringMVC (RestTemplate)*, *JavaFX*, *ControlFX*
* **RESTful** - *Spting-MVC (rest)*
* **Kolaer-web** - *Spting-MVC(rest/jsp)*, *Spting-Security*, *Hibernate*, *Swagger*, *Lombok*, *Apache POI*
* **Build** - *Gradle (Multiproject)*, *Maven (Standalone)*

## Архитектура

На трех физических серверах развернуты Jetty с RESTful серверами. Первый сервер основной и использует основную базу данных. 
Второй сервер резервный и использует основную базу данных. Третий сервер тестовый и использует тестувую базу данных.
На еще одной физической машине развернут сервер с сервлетом, который дублирует запросы на основной или резервный сервер в зависимости от установленного флага.
Отдельно был установлен Apache сервер и развернут на нем Web client на AngularJS.
Desktop client + плагины лежита на файловом сервере. Пользователт скачивает через специальный лаунчер только основной клиент (без плагинов и доп. файлы)
и при запуске использует рабочее простанство файлового сервера.

![](https://github.com/danilovEY/Kolaer-systems/blob/master/Architecture.png)

## Скриншоты

![](https://github.com/danilovEY/Kolaer-systems/blob/master/client/Screenshot.jpg)
![](https://github.com/danilovEY/Kolaer-systems/blob/master/server/Screenshot.jpg)