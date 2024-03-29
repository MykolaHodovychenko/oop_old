# Работа со Spring Security, часть 3

Аутентификация на основе сессий

Протокол HTTP не отслеживает состояния (является stateless протоколом). Это означает, что каждый запрос выполняется независимо, без информации о предыдущих запросах. Серверу не нужно хранить информацию о статусе каждого клиента на протяжении нескольких запросов от него. Такой подход позволяет минимизировать потребление ресурсов сервером, а также сократить количество данных, которые передаются между клиентом и сервером.

Многим приложениям необходимо отслеживать статус и состояние клиента, например, чтобы кастомизировать содержимое страницы для конкретного пользователя. Для реализации такого функционала есть несколько способов:

**Аутентификация на основе сессий**

Одним из способов отслеживать состояния пользователя является аутентификация на основе сессий с использованием cookies.

HTTP cookie (web cookie, куки браузера) - это небольшой фрагмент данных, который сервер отправляет браузеру пользователя. Браузер может сохранить этот фрагмент у себя и отправлять на сервер с каждым последующим запросом. Это, в частности, позволяет узнать, с одного ли сервера пришли несколько запросов. С помощью кук можно сохранить любую информацию о состоянии. Куки часто используются для:

- управления сеансом (логины, корзины для виртуальных покупок);
- персонализации (пользовательские предпочтения);
- трекинга (отслеживание поведения пользователей.)

С технической точки зрения, куки - это обычные HTTP-заголовки.

В случае сценария аутентификации, куки хранят информацию об id сессии. Сессии придуманы для того, чтобы сервер "помнил" пользователя при повторных запросах от него. То есть, пользователь вводит однократно имя и пароль, и при дальнейших запросах сервер понимает, от кого именно пришел запрос, а также какие объекты есть в данном сеансе (например, товары в корзине покупок).

>Пояснить взаимодействие с сервером можно на примере обращения в службу поддержки. При первом обращении клиент описывает проблему и получает номер обращения (**JSESSIONID**). Дальше переписка идет под этим номером обращения. Клиенту не надо каждый раз заново все пересказывать. Служба поддержки (сервер) по номеру сама восстанавливает все детали (идентичность пользователя и данные сессии).

Реализуется это с помощью идентификаторов сессий. Стандартный алгоритм следующий:

1 Сервер высылает клиенту при первом запросе (например, при успешном логине, но можно и анонимному клиенту) заголовок типа

```Set-Cookie: JSESSIONID=4C7871D1EF406F69C7CF20CD6BD283F1```

2 Браузер сохраняет эти значения (свои для каждого сайта), и далее при каждом запросе на конкретный сайт браузер автоматически добавляет к запросу соответствующий заголовок:

```Cookie: JSESSIONID=4C7871D1EF406F69C7CF20CD6BD283F1```

> Название JSESSIONID не универсально, а характерно именно для Java. В других языках используются другие названия.

При последующих запросах от того же клиента, сервер опознает клиента по идентификатору сессии. Контейнер хранит эти идентификаторы сессий и соответствующие данные клиента как словарь в Map. Сессия имеет срок годности. Как только она истекает, данные исчезают, и в последующих запросах контейнер не принимает истекший Cookie конкретного клиента.

Метод аутентификации JWT является одним из самых распространенных методов аутентификации запросов на WEB-приложения и позволяет защитить REST API.

Данный вид защиты и аутентификации имеет ряд преимуществ:

- удобство (не нужно при каждом запросе передавать логин и пароль);
- меньше запросов к БД (токен может содержать в себе базовую информацию о пользователе);
- простота реализации (достаточно использовать готовую библиотеку для генерации и расшифровки токена).

Токен - это просто строка, которая генерируется по запросу пользователя, который хочет в дальнейшем вызывать защищенные ресурсы.