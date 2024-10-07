# Px32 Bot
- [JDA](https://github.com/discord-jda/JDA)로 만든 Kotlin 디스코드 봇 구동기 입니다.

## How to Use
봇 구동기는 [이곳](https://gitlab.wh64.net/devproje/px32-bot/-/releases)에서 다운로드 받으실 수 있습니다.

## Developments
API를 사용하여 플러그인 개발을 원하신다면 아래의 코드로 라이브러리를 추가 해 주세요.
JDA의 버전은 [이곳](https://github.com/discord-jda/JDA/releases)에서 확인 하실 수 있습니다.

- Maven
```xml
<repositories>
    <repository>
        <id>ProjectCentral</id>
        <url>https://repo.wh64.net/repository/maven-releases/</url>
    </repository>

    // If you want to snapshot version, please write this code.
    <repository>
        <id>ProjectCentral-SNAPSHOT</id>
        <url>https://repo.wh64.net/repository/maven-snapshots/</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    ...
    <!-- JDA is required -->
    <dependency>
        <groupId>net.dv8tion</groupId>
        <artifactId>JDA</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>net.projecttl</groupId>
        <artifactId>px32-bot-api</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

- Gradle
```kts
repositories {
    mavenCentral()
    maven("https://repo.wh64.net/repository/maven-releases/")
    
    // If you want to snapshot version, please write this code.
    maven("https://repo.wh64.net/repository/maven-snapshots/")
}
```

```kts
dependencies {
    ...
    // JDA is required
    compileOnly("net.dv8tion:JDA:VERSION")
    compileOnly("net.projecttl:px32-bot-api:VERSION")
}
```

## License
- 해당 프로젝트는 [MIT License](https://gitlab.wh64.net/devproje/px32-bot/-/blob/master/LICENSE)를 따릅니다.
