# 🚀 TinyX

## 📌 Description
Ce projet est une **application de micro-blogging** construite en **architecture microservices** avec **Quarkus** et des bases **NoSQL**. Il permet aux utilisateurs de publier des messages courts, d'interagir via likes et abonnements, et d'accéder à un fil d’actualité personnalisé.

## 🏗️ Architecture
L'application est composée de plusieurs **microservices** indépendants qui communiquent via **REST API et Redis** :

### **🔹 Liste des services :**
| Service | Rôle |
|---------|------|
| **repo-post** | Gestion des publications (création, suppression, récupération) |
| **repo-social** | Gestion des interactions sociales (likes, follows, blocks) |
| **srvc-search** | Indexation et recherche des posts (ElasticSearch) |
| **srvc-user-timeline** | Fil d’actualité personnel (posts et likes de l’utilisateur) |
| **srvc-home-timeline** | Fil d’actualité des abonnements |
| **api-gateway** | Point d’entrée unique pour les utilisateurs |
| **infra** | Déploiement Kubernetes avec Kustomize |

---

---

## 📂 Structure du projet

```
2025-epitweet-tinyx-29/
│── repo-post/               # Service de gestion des publications
│── repo-social/             # Service des interactions sociales
│── srvc-search/             # Service de recherche
│── srvc-user-timeline/      # Service de timeline utilisateur
│── srvc-home-timeline/      # Service de timeline des abonnements
│── api-gateway/             # API Gateway et orchestration
│── infra/                   # Déploiement Kubernetes
│── scripts/                 # Scripts utiles (DB init, migrations...)
│── docs/                    # Documentation du projet
│── tests/                   # Tests d'intégration
│── README.md                # Documentation principale
│── docker-compose.yml       # Déploiement local rapide
│── .gitignore               # Fichiers à exclure
```

---

## 🌐 Attribution des ports des microservices (Quarkus)

| Microservice             | Port interne (Quarkus) | Port exposé (Docker/K8S) | Description |
|--------------------------|----------------------|-------------------------|-------------|
| **repo-post**           | 8081                 | 30081                    | Gestion des publications (posts) |
| **repo-social**         | 8082                 | 30082                    | Gestion des interactions sociales (likes, follows, blocks) |
| **srvc-search**         | 8083                 | 30083                    | Indexation et recherche (ElasticSearch) |
| **srvc-user-timeline**  | 8084                 | 30084                    | Fil d'actualité personnel (posts et likes de l'utilisateur) |
| **srvc-home-timeline**  | 8085                 | 30085                    | Fil d'actualité des abonnements |
| **api-gateway**         | 8080                 | 30080                    | Point d’entrée unique pour l'API REST |


---
