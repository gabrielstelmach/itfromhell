# README
Railsdex is a Ruby on Rails application designed to help developers get familiar with setting and starting up a 
modern Rails development environment while showcasing **CRUD**, **API**, **cloud storage**, and diverse best practices 
using shining gems in a simple, approachable way. You may start this trail from [Setting Ruby on Rails](https://itfromhell.net/2025/04/09/setting-ruby-on-rails/).

## Versions
* Ruby 3.4.2
* Rails 8.0.2

## Application dependencies

* Active Storage Validations: active_storage_validations
* Amazon SDK S3: aws-sdk-s3, version >= 1.142 and < 2.0
* MySQL: mysql2
* Nokogiri: nokogiri, version 1.18.8
* Psych: psych

## Rails commands
### Start the application server
```
rails server
```
### Download and install all required gems and their dependencies
```
bundle install
```
### Apply pending migration files
```
rails db:migrate
```
### Compile assets
```
rails assets:precompile
```
## Application URL
http://localhost:3000

## API documentation
### Base URL
http://localhost:3000/api

### Endpoints version 1

#### List all Pokémons
**GET** `/v1/pokemons`
- **Description:** Retrieve a list of all Pokémons.
- **Response:**
```
[
    {
        "id": 1,
        "name": "Pikachu",
        "description": "Electric-type Pokémon. It's recognizable...",
        "captured": false,
        "created_at": "2025-05-15T19:25:58.513Z",
        "updated_at": "2025-05-22T23:17:58.064Z",
        "elemental_type_id": 4
    },
    ...
]
```
- **Status Codes:** `200 OK`
- **Example of cURL request**
```
curl http://localhost:3000/api/v1/pokemons
```

#### Get a single Pokémon
**GET** `/v1/pokemons/{id}`
- **Description:** Retrieve details for a specific Pokémon.
- **Response:**
```
{
    "id": 1,
    "name": "Pikachu",
    "description": "Electric-type Pokémon. It's recognizable...",
    "captured": false,
    "created_at": "2025-05-15T19:25:58.513Z",
    "updated_at": "2025-05-22T23:17:58.064Z",
    "elemental_type_id": 4
}
```
- **Status Codes:** `200 OK`

### Endpoints version 2

#### List all Pokémons
**GET** `/v2/pokemons`
- **Description:** Retrieve a list of all Pokémons.
- **Response:**
```
[
    {
        "id": 1,
        "name": "Pikachu",
        "description": "Electric-type Pokémon. It's recognizable...",
        "captured": false,
        "created_at": "2025-05-15T19:25:58Z",
        "updated_at": "2025-05-22T23:17:58Z",
        "elemental_type": {
            "id": 4,
            "name": "Electric"
        },
        "picture_url": "http://localhost:3000/rails/active_storage/disk/eyJfcmFpbHMiOnsiZGF0YSI6eyJrZXkiOiJiYzV4dWlteGZneXRvcXZpdTRibWRnZGNmYm5jIiwiZGlzcG9zaXRpb24iOiJpbmxpbmU7IGZpbGVuYW1lPVwiY2hhci1waWthY2h1LnBuZ1wiOyBmaWxlbmFtZSo9VVRGLTgnJ2NoYXItcGlrYWNodS5wbmciLCJjb250ZW50X3R5cGUiOiJpbWFnZS9wbmciLCJzZXJ2aWNlX25hbWUiOiJsb2NhbCJ9LCJleHAiOiIyMDI1LTA1LTI3VDAyOjA2OjA3LjU3N1oiLCJwdXIiOiJibG9iX2tleSJ9fQ==--10a0af9a0c13d05497f97572aa2ba2c951271763/char-pikachu.png"
    },
    ...
]
```
- **Status Codes:** `200 OK`
- **Example of cURL request**
```
curl -H "Accept: text/xml" http://localhost:3000/api/v2/pokemons
```

#### Get a single Pokémon
**GET** `/v2/pokemons/{id}`
- **Description:** Retrieve details for a specific Pokémon.
- **Response:**
```
{
    "id": 1,
    "name": "Pikachu",
    "description": "Electric-type Pokémon. It's recognizable...",
    "captured": false,
    "created_at": "2025-05-15T19:25:58Z",
    "updated_at": "2025-05-22T23:17:58Z",
    "elemental_type": {
        "id": 4,
        "name": "Electric"
    },
    "picture_url": "http://localhost:3000/rails/active_storage/disk/eyJfcmFpbHMiOnsiZGF0YSI6eyJrZXkiOiJiYzV4dWlteGZneXRvcXZpdTRibWRnZGNmYm5jIiwiZGlzcG9zaXRpb24iOiJpbmxpbmU7IGZpbGVuYW1lPVwiY2hhci1waWthY2h1LnBuZ1wiOyBmaWxlbmFtZSo9VVRGLTgnJ2NoYXItcGlrYWNodS5wbmciLCJjb250ZW50X3R5cGUiOiJpbWFnZS9wbmciLCJzZXJ2aWNlX25hbWUiOiJsb2NhbCJ9LCJleHAiOiIyMDI1LTA1LTI3VDAyOjA2OjA3LjU3N1oiLCJwdXIiOiJibG9iX2tleSJ9fQ==--10a0af9a0c13d05497f97572aa2ba2c951271763/char-pikachu.png"
}
```
- **Status Codes:** `200 OK`, `404 Not Found`
- **Example of cURL request**
```
curl -H "Accept: text/csv" http://localhost:3000/api/v2/pokemons/1
```

#### Create a new Pokémon
**POST** `/v2/pokemons`
- **Description:** Create a new Pokémon with details like its capture.
- **Request body:**
```
{
    "name": "Eevee",
    "description": "Eevee is a small, fox-like Pokémon known for its cute appearance and unique ability to evolve into multiple forms.",
    "captured": false,
    "elemental_type_id": 6
}
```
- **Response:**
```
{
    "id": 10,
    "name": "Eevee",
    "description": "Eevee is a small, fox-like Pokémon known for its cute appearance and unique ability to evolve into multiple forms.",
    "captured": false,
    "created_at": "2025-05-27T02:10:35Z",
    "updated_at": "2025-05-27T02:10:35Z",
    "elemental_type": {
        "id": 6,
        "name": "Fighting"
    }
}
```
- **Status Codes:** `201 Created`, `442 Unprocessable Entity`
- **Example of cURL request**
```
curl -X POST http://localhost:3000/api/v2/pokemons \
  -H "Accept: application/json" \
  -F "name=Mewtwo" \
  -F "captured=false" \
  -F "elemental_type_id=15" \
  -F "picture=@/c/pokemons/Mew.png"
```

#### Update a Pokémon
**PUT** `/v2/pokemons/{id}`
- **Description:** Update an existing Pokémon with details like picture.
- **Request body:**
```
{
    "captured": true,
    "picture": "data:image/png;base64./9j/4AAQSkZJRgABAQEAAAAAAAD/2wBDAAoHBwkHBgoJCAkLCwoMDx..."
}
```
- **Response:**
```
{
    "id": 10,
    "name": "Eevee",
    "description": "Eevee is a small, fox-like Pokémon known for its cute appearance and unique ability to evolve into multiple forms.",
    "captured": true,
    "created_at": "2025-05-27T02:10:35Z",
    "updated_at": "2025-05-27T02:10:35Z",
    "elemental_type": {
        "id": 6,
        "name": "Fighting"
    }
    "picture_url": "http://localhost:3000/rails/active_storage/disk/eyJfcmFpbHMiOnsiZGF0YSI6eyJrZXkiOiJwc3V3NnJnN2wzM25uaHE1dG1jNDgyaHI5eG5uIiwiZGlzcG9zaXRpb24iOiJpbmxpbmU7IGZpbGVuYW1lPVwiZWV2ZWUucG5nXCI7IGZpbGVuYW1lKj1VVEYtOCcnZWV2ZWUucG5nIiwiY29udGVudF90eXBlIjoiaW1hZ2UvcG5nIiwic2VydmljZV9uYW1lIjoibG9jYWwifSwiZXhwIjoiMjAyNS0wNS0yN1QwMjoyODowMy4zMTJaIiwicHVyIjoiYmxvYl9rZXkifX0=--34f80e3651701444fae12bad25eeb83584ecee04/eevee.png"
}
```
- **Status Codes:** `202 Accepted`, `404 Not Found`, `442 Unprocessable Entity`
- **Example of cURL request**
```
curl -X PUT http://localhost:3000/api/v2/pokemons/8 \
  -H "Accept: application/json" \
  -H 'Content-Type: application/json' \
  -d '{
    "captured": true,
  }'
```

#### Remove a Pokémon
**DELETE** `/v2/pokemons/{id}`
- **Description:** Remove an existing Pokémon.
- **Status Codes:** `204 No Content`, `404 Not Found`
- **Example of cURL request**
```
curl -X DELETE http://localhost:3000/api/v2/pokemons/4
```

## Project file structure
This section outlines only the assets and files directly involved with the application.
```
.
├── app/
│   ├── assets/
│   ├── controllers/
│   │   ├── api/
│   │   │   ├── v1/
│   │   │   │   └── pokemons_controller.rb
│   │   │   └── v2/
│   │   │       └── pokemons_controller.rb
│   │   ├── application_controller.rb
│   │   ├── home_controller.rb
│   │   └── pokemons_controller.rb
│   ├── helpers/
│   │   └── api/
│   │       └── v2/
│   │           └── pokemons_helper.rb
│   ├── javascript/
│   │   ├── controllers/
│   │   └── application.js
│   ├── models/
│   │   ├── application_record.rb
│   │   ├── elemental_type.rb
│   │   └── pokemon.rb
│   ├── serializers/
│   │   └── pokemon_json_serializer.rb
│   ├── services/
│   │   └── pokemon_xml_generator_service.rb
│   │── views/
│   │   │── home/
│   │   │   └── index.html.erb
│   │   │── layouts/
│   │   │   └── application.html.erb
│   │   └── pokemons/
│   │       │── edit.html.erb
│   │       │── index.html.erb
│   │       │── new.html.erb
│   │       │── show.html.erb
│   │       │── ...
├── config/
│   ├── environments/
│   │   ├── development.rb
│   │   ├── production.rb
│   │   └── test.rb
│   ├── storage.yml
│   ├── routes.rb
│   ├── ...
├── db/
│   └── migrate/
│   │   └── ...
│   └── seeds.rb
├── Gemfile
├── README.md
├── ...
```