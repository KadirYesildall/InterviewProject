# Repsy Package Manager – FullStack Dev Assignment

A RESTful Spring Boot service to upload and download `.rep` packages using pluggable storage strategies.

---

## 🚀 Features

- Upload `.rep` & `meta.json` for a package/version
- Download those files by name/version
- Uses either:
  - 📁 Local FileSystem
  - ☁️ MinIO Object Storage
- Metadata stored in PostgreSQL
- Environment-configurable
- Fully dockerized

---

## 📦 API Endpoints

### ✅ Upload a package

```http
POST /{packageName}/{version}
Form-data:
  package: file (.rep)
  meta: file (meta.json)
