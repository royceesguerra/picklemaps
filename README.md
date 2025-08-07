# PickleMaps

**PickleMaps** is a full-stack web platform for discovering and reviewing pickleball courts.

🔗 [Live Site](https://picklemaps.royceesguerra.site)

## Features

- Secure user authentication via Keycloak
- Authenticated users can add, update, and delete courts and reviews with photo uploads
- Advanced, typo-tolerant search powered by Elasticsearch
- Filter courts based on rating
- Shows nearby pickleball courts
- Review system with sorting by rating or date
- Real-time logging and monitoring using Kibana

## Tech Stack

**Backend**

- Java, Spring Boot, Spring Security
- PostgreSQL, Elasticsearch, Kibana
- Keycloak (OAuth2 authentication)

**Frontend**

- Next.js, TypeScript
- Tailwind CSS

**DevOps & Deployment**

- Docker, Docker Compose, Nginx
- GitHub Actions (CI/CD)
- DigitalOcean (Hosting)
- TLS/SSL via Let’s Encrypt
