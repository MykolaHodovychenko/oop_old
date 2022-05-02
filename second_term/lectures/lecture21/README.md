# Работа со Spring Security, часть 2


На данном занятии мы разберем использование Spring Security для регистрации и последующей аутентификации пользователя.

Создадим новый Spring MVC проект. В нашем проекте будет 4 окна: окно логина, окно регистрации, приветственная страница для пользователя и приветственная страница для администратора.

Создадим шаблон для страницы регистрации

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
          crossorigin="anonymous">

    <title>Создание аккаунта</title>
</head>
<body class="bg-light">
<div class="container">
    <div class="py-3 text-center">
        <h2>Создание аккаунта</h2>
        <p class="lead">Введите имя пользователя и пароль</p>
    </div>
    <form method="post" enctype="multipart/form-data" action="#">
            <div class="col-md-4">
                <div class="row">
                    <div class="mb-2">
                        <label for="username">Username</label>
                        <input type="text" class="form-control" id="username" placeholder="" value="">
                    </div>
                </div>
                <div class="row">
                    <div class="mb-2">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" placeholder="" value="">
                    </div>
                </div>
                <div class="row">
                    <div class="mb-3">
                        <label for="confirm_password">Confirm password</label>
                        <input type="password" class="form-control" id="confirm_password" placeholder="" value="">
                    </div>
                </div>
                <button class="btn btn-primary btn-lg btn-block" type="submit" value="Submit">Создать аккаунт</button>
                <a class="btn btn-lg btn-block btn-outline-primary" role="button" aria-disabled="true">Назад</a>
            </div>
    </form>
</div>

<br/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
```

Страница будет выглядеть следующим образом

<p align="center">
  <img src="img/img_01.png" />
</p>

При нажатии на кнопку "Создать аккаунт" должно 

Самым фундаментальным объектом является `SecurityContextHolder`. В нем хранится информация о текущем контексте безопасности приложения, который включает в себя подробную информацию о пользователе (principal), работающим с приложением. Spring Security использует объект `Authentication`, пользователя авторизованной сессии.

"Пользователь" - это просто объект. В большинстве случаев он может быть приведен к классу `UserDetails`. `UserDetails` можно представить, как адаптер между БД пользователей и тем, что требуется Spring Security внутри `SecurityContextHolder`.

Для создания `UserDetails` используется интерфейс `UserDetailsService`, с единственным методом.

```java
UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
```

В пакете `model` создадим класс сущности `Role`, который будет хранить роли в системе.

```java
@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
```

Обратите внимание, что таблицы `Role` и `User` будут находиться в отношении "многие-ко-многим".

Далее, в пакете `model`, создадим класс сущности `User`, который моделирует пользователя в системе.

```java
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToMany
    @JoinTable(
            name ="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
```

Аннотация `@Transient` означает, что данное поле не будет сериализовано при добавлении сущности в БД.

Далее необходимо реализовать доступ к данным. Соединение с базой является важнейшей составляющей приложения. Как правило, выделяется часть кода, модуль, отвечающий за передачу запросов в БД и обработку полученных от нее ответов.

Для доступа к базе данных, как правило, используется шаблон проектирования **DAO** (Data Access Object). DAO - это абстрактный интерфейс к какому-либо типу базы данных или механизму хранения. По сути, DAO это прослойка между БД и системой. В Spring Data этот слой называется **Repository**.

Создадим интерфейс `RoleDao` и `UserDao` в пакете `dao`.

```java
public interface RoleDao extends JpaRepository<Role, Long> {
}
```

Обратите внимание, что для сущности `UserDao` мы определили метод интерфейса, который будет возвращать объект `User` по `username` пользователя.

```java
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
```

Далее необходимо создать классы для слоя сервисов. В пакете `services` объявим интерфейс `UserService` и класс `UserServiceImpl`, который реализует этот интерфейс.

```java
public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
```

```java
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getById(1L));
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
```

Далее необходимо написать реализацию интерфейса `UserDetailsService` - класс `UserDetailsServiceImpl`.

Интерфейс `UserDetailsService` используется для получения данных пользователя. В нем объявлен всего один метод `loadUserByUsername()`.

```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

Этот метод принимает введенный пользователем логин и использует его для получения объекта `UserDetails`. Если объект с таким логином получить не удалось, метод выбрасывает исключение `UsernameNotFoundException`.

Возвращаемый `UserDetails` представляет собой интерфейс, который предоставляет геттеры, которые гарантируют not-null результаты аутентификационной информации, такие как имя пользователя, пароль, предоставленные полномочия и является ли учетная запись пользователя заблокированной или нет.

```java
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
```