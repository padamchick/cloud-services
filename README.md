# Aplikacja do zarządzania wnioskami

## Opis
Celem aplikacji jest zarządzanie wnioskami. Umożliwia ona tworzenie wniosków, zarządzanie ich treścią oraz aktualizację stanu, w jakim aktualnie się znajdują. Dodatkowo przechowywana jest pełna historia zmian stanu wniosków. Użytkownik może w każdej chwili podejrzeć wnioski znajdujące się w systemie oraz może je filtrować po nazwie oraz aktualnym stanie.
Możliwe stany wniosku prezentuje diagram stanów poniżej:

![States](https://i.ibb.co/yVNQD8S/image.png)

## Rozwiązanie
* do filtrowania wniosków wykorzystano Criteria API
* historia zmian stanu wniosku śledzona dzięki Hibernate Envers
* dokumentacja API wygenerowana przy narzędzia Swagger i dostępna pod adresem `http://localhost:8080/swagger-ui.html`
* wykorzystano relacyjną bazę danych PostgreSQL uruchomioną na Dockerze
* do automatycznego mapowania klas encyjnych na DTO wykorzystano framework MapStruct

## Kontakt
Utworzono przez [padamchick](https://github.com/padamchick). Masz pytanie? Pisz śmiało!
