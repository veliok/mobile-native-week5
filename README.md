# Viikko 5: Sääsovellus

## Esittely
- **Video:** <https://youtu.be/qDf86nFk2_0>

## Mitä Retrofit tekee
**Retrofit** on tyyppiturvallinen HTTP-kirjasto, jota käytetään verkkokutsujen hallintaan. Vähentään paljon koodin kirjoittamisen tarvetta määrittelyillä ja automaattisella JSON-jäsennyksellä. Sovelluksessa se toimii siltana sovelluksen ja OpenWeather-palvelimen välillä.

## Miten JSON muutetaan dataluokiksi
API palauttaa tiedot JSON-muodossa, **Gson-kirjasto** hoitaa muunnoksen automaattisesti Kotlin-olioksi(**JSON deserialization**). Retrofit-instanssiin määritelty ```GsonConverterFactory```lukee JSON-avainten nimet ja sovittaa ne Kotlinin ```WeatherResponse```-dataluokan kenttiin.

## Miten coroutines toimii tässä
Verkkokutsuja ei saa suorittaa sovelluksen pääsäikeessä, jotta käyttöliittymä ei jäädy. Tässä sovelluksessa **Coroutinet** suorittavat API-kutsun **taustasäikeessä**. Käytössä on ```viewModelScope.launch```, joka varmistaa, että haku keskeytyy automaattisesti, jos ViewModel tuhoutuu. Kun data on haettu, coroutine palauttaa tuloksen pääsäikeeseen UI-tilan päivitystä varten.

## Miten UI-tila toimii
ViewModel hallitsee UI:n tilaa ```WeatherUiState```-dataluokan avulla. Se käynnistää verkkopyynnöt viewModelScope-coroutine-scopessa, joka peruuntuu automaattisesti kun käyttäjä poistuu näkymästä. StateFlow välittää tilan muutokset UI:lle.
- **ViewModel**: Päivittää tilaa (esim. ```isLoading = true``` tai uusi ```weather```-olio).
- **Jetpack Compose**: Tarkkailee tätä tilaa (```StateFlow```). Kun tila muuttuu, Compose suorittaa automaattisesti uudelleenpiirron

## Miten API-key on tallennettu
API-avain on suojattu käyttämällä ```local.properties```-tiedostoa, jota ei tallenneta versionhallintaan (Git).
1. Avain kirjoitetaan ```local.properties```-tiedostoon.
2. ```build.gradle.kts```-tiedosto lukee arvon ja luo siitä ```BuildConfig```-vakion.
3. Koodissa avainta kutsutaan muodossa ```BuildConfig.OPENWEATHER_API_KEY```, josta Retrofit saa sen käyttöönsä.
