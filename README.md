# Adaptive Learning AI

## 1. Tầm nhìn sản phẩm

Adaptive Learning AI là nền tảng luyện thi chuyên sâu theo mục tiêu (Target-driven Exam Prep). Hệ thống không cố gắng cá nhân hóa mọi hoạt động học tập một cách chung chung mà tập trung vào một kết quả đo được:

> Giúp thí sinh biết chính xác mình đang ở đâu, cần đạt gì, yếu ở đâu và phải luyện nội dung nào để tiến gần mục tiêu trong thời gian ngắn nhất.

Ví dụ mục tiêu:

- IELTS 6.5, 7.0 hoặc 8.0.
- TOEIC 700 hoặc 800.
- Thi tốt nghiệp THPT.
- HSK, chứng chỉ nghề hoặc kỳ thi nội bộ của tổ chức.

AI là năng lực lõi của sản phẩm, được dùng để phân tích dữ liệu học tập, chấm bài tự luận, đánh giá Speaking, phát hiện lỗi, xây dựng lộ trình và tạo nội dung luyện tập. Các quyết định quan trọng về điểm số phải có dữ liệu, rubric, quy tắc kiểm tra và khả năng giải thích; AI không được là một hộp đen tuyệt đối.

---

## 2. Mục tiêu kinh doanh và giá trị người dùng

### 2.1. Giá trị cho thí sinh

- Đánh giá đầu vào để biết trình độ hiện tại.
- Thi thử giống kỳ thi thật về thời lượng, cấu trúc, độ khó và cách tính điểm.
- Nhận kết quả theo từng kỹ năng, dạng câu hỏi, chủ đề và mức độ khó.
- Được AI chỉ rõ lỗi sai, nguyên nhân, cách sửa và bài mẫu tham khảo.
- Có lộ trình luyện tập thay đổi theo dữ liệu thực tế.
- Biết nên học gì tiếp theo thay vì làm đề ngẫu nhiên.
- Theo dõi xác suất đạt mục tiêu và tiến bộ theo thời gian.

### 2.2. Giá trị cho giáo viên và người biên soạn nội dung

- Quản lý ngân hàng câu hỏi có metadata đầy đủ.
- Tạo đề theo blueprint, kỹ năng, chủ đề và độ khó.
- Xem thống kê chất lượng câu hỏi.
- Kiểm duyệt nội dung do AI sinh ra.
- Thiết lập rubric chấm Writing/Speaking.
- Theo dõi các lỗi phổ biến của học viên.

### 2.3. Giá trị cho quản trị viên

- Quản lý người dùng, kỳ thi, mục tiêu, đề thi và nội dung.
- Kiểm duyệt nội dung trước khi phát hành.
- Theo dõi hoạt động, chất lượng AI, chi phí model và các lỗi hệ thống.
- Quản lý phiên bản rubric, prompt và mô hình AI.

---

## 3. Phạm vi phiên bản đầu tiên

### MVP bắt buộc

1. Đăng ký, đăng nhập, phân quyền học viên/biên soạn viên/admin.
2. Quản lý chứng chỉ, môn thi, kỹ năng, chủ đề và dạng câu hỏi.
3. Quản lý ngân hàng câu hỏi khách quan và tự luận.
4. Tạo đề thi, blueprint đề và phát hành đề.
5. Làm bài thi có timer phía client và kiểm tra thời gian phía server.
6. Lưu đáp án nháp, nộp bài, tự động chấm câu khách quan.
7. Chấm Writing bằng AI theo rubric có cấu trúc.
8. Phân tích lỗi theo kỹ năng, topic, question type và difficulty.
9. Gợi ý gói luyện tập dựa trên lỗ hổng.
10. Dashboard tiến bộ và lịch sử làm bài.

### Giai đoạn mở rộng

- Speaking realtime hoặc ghi âm nhiều lượt.
- Speech-to-text và phân tích phát âm, fluency, lexical resource.
- Sinh câu hỏi và sinh đề bằng AI có kiểm duyệt.
- RAG với tài liệu chính thống của từng chứng chỉ.
- Dự báo điểm và xác suất đạt mục tiêu.
- Adaptive test tự thay đổi độ khó theo năng lực.
- Trợ giảng AI hội thoại theo từng lỗi của học viên.
- Thanh toán, gói thuê bao và báo cáo cho trung tâm.

---

## 4. Vai trò trong hệ thống

### STUDENT

- Chọn chứng chỉ và mục tiêu điểm.
- Làm bài kiểm tra đầu vào, mock test và practice package.
- Nộp Writing/Speaking.
- Xem kết quả, feedback, gap analysis và lộ trình.

### CONTENT_EDITOR / TEACHER

- Tạo, sửa, nhập và kiểm duyệt câu hỏi.
- Tạo đề, rubric và gói luyện.
- Xem thống kê câu hỏi và kết quả học viên trong phạm vi được cấp quyền.

### ADMIN

- Quản lý toàn bộ người dùng, nội dung, kỳ thi, quyền hạn và cấu hình AI.
- Duyệt nội dung trước khi public.
- Theo dõi audit log, model usage, chi phí và lỗi AI.

### AI_WORKER

- Xử lý job bất đồng bộ: chấm bài, speech-to-text, phân tích lỗi, sinh nội dung.
- Không đăng nhập như người dùng và chỉ được truy cập dữ liệu theo job được cấp.

---

## 5. Các luồng nghiệp vụ chính

### 5.1. Onboarding và thiết lập mục tiêu

1. Người dùng đăng ký và xác thực tài khoản.
2. Chọn chứng chỉ/kỳ thi.
3. Chọn điểm mục tiêu, ngày thi dự kiến, số giờ học mỗi tuần.
4. Làm bài diagnostic test hoặc nhập điểm hiện tại.
5. Hệ thống tính baseline theo từng kỹ năng.
6. AI tạo mục tiêu ngắn hạn, tuần và ngày.
7. Người dùng có thể thay đổi mục tiêu; hệ thống phải version hóa kế hoạch cũ.

Thông tin mục tiêu gồm: chứng chỉ, band/score mục tiêu, ngày thi, điểm hiện tại, kỹ năng ưu tiên, thời gian rảnh, ngôn ngữ feedback và phương pháp học ưa thích.

### 5.2. Quản lý ngân hàng câu hỏi

Mỗi câu hỏi cần có:

- Nội dung, loại nội dung, media và đáp án.
- Certificate, subject, skill, topic, subtopic.
- Question type, difficulty, estimated time, points.
- Explanation, source, version, author, reviewer.
- Tags, learning objectives và prerequisite.
- Trạng thái `DRAFT`, `IN_REVIEW`, `APPROVED`, `PUBLISHED`, `ARCHIVED`.
- Thống kê attempts, accuracy, discrimination index và độ ổn định.

Các dạng câu hỏi cần hỗ trợ:

- Multiple choice, multiple select.
- True/False/Not Given.
- Matching, ordering, map labelling.
- Fill in the blank, short answer.
- Essay/Writing.
- Audio response/Speaking.

AI có thể gợi ý câu hỏi, đáp án, distractor, explanation và metadata nhưng nội dung public phải qua kiểm duyệt của con người.

### 5.3. Tạo đề thi và blueprint

Đề thi được tạo thủ công hoặc tự động theo blueprint:

- Số câu theo kỹ năng và topic.
- Tỷ lệ difficulty.
- Phân bổ question type.
- Thời lượng và tổng điểm.
- Điều kiện bắt buộc của passage/audio.
- Quy tắc tính điểm và quy đổi band.

Quy trình: `DRAFT -> REVIEW -> APPROVED -> PUBLISHED -> ARCHIVED`. Đề đã có attempt không được sửa phá vỡ dữ liệu cũ; mọi thay đổi phải tạo version mới.

### 5.4. Mock Test Engine

Luồng chuẩn:

1. Học viên chọn đề và bấm bắt đầu.
2. Server tạo `attempt` với `startedAt`, `expiresAt`, random seed và snapshot version của đề.
3. API chỉ trả câu hỏi, không trả đáp án đúng.
4. Client hiển thị countdown nhưng server là nguồn thời gian chính.
5. Đáp án được autosave theo từng câu hoặc mỗi 15-30 giây.
6. Học viên nộp bài hoặc hệ thống tự nộp khi hết giờ.
7. Server khóa attempt, chấm câu khách quan và tạo AI grading jobs nếu cần.
8. Kết quả chuyển qua `SUBMITTED -> GRADING -> GRADED`.

Anti-cheat mức nhẹ:

- Ghi nhận tab switch, blur, fullscreen exit, copy/paste và refresh.
- Lưu IP, user agent, thời gian và loại vi phạm.
- Cảnh báo ở ngưỡng cấu hình được.
- Có thể auto-submit khi vượt ngưỡng.
- Không tuyên bố đây là chống gian lận tuyệt đối; mọi cờ vi phạm phải hiển thị minh bạch.

### 5.5. Chấm điểm

- Câu khách quan: chấm ngay bằng đáp án snapshot của attempt.
- Câu tự luận: tạo job chấm AI hoặc chờ giáo viên duyệt.
- Điểm phải lưu cả raw score và normalized score.
- Lưu scoring rule version để kết quả cũ tái hiện được.
- Cho phép teacher override có lý do và audit log.
- Không cho phép sửa đáp án đúng làm thay đổi điểm lịch sử mà không tạo regrade job.

---

## 6. Năng lực AI cốt lõi

### 6.1. AI chấm Writing

Input:

- Bài viết của học viên.
- Prompt/task và yêu cầu số từ.
- Certificate, skill và rubric version.
- Lịch sử lỗi liên quan nếu được phép dùng.

Output bắt buộc dạng JSON có schema kiểm tra được:

```json
{
  "overallScore": 6.5,
  "criteria": [
    {
      "name": "Task Achievement",
      "score": 6.0,
      "feedback": "...",
      "evidence": ["..."]
    }
  ],
  "errors": [
    {
      "category": "GRAMMAR",
      "severity": "MEDIUM",
      "original": "He go to school",
      "correction": "He goes to school",
      "explanation": "...",
      "position": {"start": 10, "end": 28}
    }
  ],
  "strengths": ["..."],
  "nextActions": ["..."],
  "rewriteSuggestion": "...",
  "confidence": 0.86
}
```

AI phải phân biệt rõ: lỗi quan sát được, suy luận, gợi ý cải thiện và điểm số. Không được bịa nguồn hoặc khẳng định tuyệt đối khi confidence thấp.

### 6.2. AI xử lý Speaking

Pipeline:

1. Upload audio theo signed URL.
2. Kiểm tra định dạng, thời lượng và chất lượng âm thanh.
3. Speech-to-text có timestamp.
4. Phát hiện đoạn không nghe rõ và yêu cầu ghi lại nếu cần.
5. Chấm transcript theo rubric.
6. Phân tích fluency, lexical range, grammar, pronunciation và coherence.
7. Trả feedback theo đoạn thời gian, không chỉ một điểm tổng.
8. Cho phép học viên nghe lại và ghi âm phiên bản cải thiện.

Không dùng giọng nói để suy luận thuộc tính nhạy cảm. Audio và transcript phải có chính sách lưu/xóa rõ ràng.

### 6.3. Sinh nội dung bằng AI

AI có thể hỗ trợ:

- Sinh câu hỏi theo learning objective.
- Sinh distractor và giải thích đáp án.
- Sinh passage, prompt, rubric draft.
- Tạo biến thể câu hỏi chống học thuộc.
- Tạo practice set theo gap.

Quy trình an toàn:

`AI_GENERATED -> AUTOMATED_VALIDATION -> HUMAN_REVIEW -> APPROVED`.

Automated validation phải kiểm tra JSON schema, đáp án duy nhất, không mâu thuẫn, mức độ khó, độ dài, nội dung nhạy cảm và trùng lặp.

### 6.4. Gap Analysis và Recommendation AI

Nguồn dữ liệu:

- Accuracy theo skill/topic/type/difficulty.
- Thời gian từng câu.
- Các lỗi Writing/Speaking.
- Số lần xem explanation.
- Mức độ hoàn thành practice package.
- Xu hướng điểm theo thời gian.

Kết quả:

- Critical gap, weak, developing, strong, mastered.
- Nguyên nhân có bằng chứng.
- Mục tiêu ưu tiên tuần này.
- Danh sách câu hỏi/gói luyện phù hợp.
- Lý do đề xuất và tiêu chí hoàn thành.

Nên triển khai rule-based trước, sau đó dùng AI để diễn giải và tối ưu thứ tự. AI không được tự đề xuất nội dung không liên quan đến skill/topic đang thiếu bằng chứng.

### 6.5. Adaptive Learning

Sau mỗi session, hệ thống cập nhật năng lực theo skill/topic. Phiên bản đầu có thể dùng weighted accuracy:

- Câu đúng gần đây có trọng số cao hơn.
- Câu difficulty cao có trọng số lớn hơn.
- Phạt khi đoán đúng nhiều lần hoặc bỏ câu.
- Tách mastery score khỏi exam score.

Giai đoạn sau có thể áp dụng Item Response Theory, Bayesian Knowledge Tracing hoặc mô hình tương tự. Mọi model phải có version và có fallback khi thiếu dữ liệu.

### 6.6. AI Tutor và RAG

AI Tutor trả lời dựa trên:

- Nội dung giải thích đã được duyệt.
- Tài liệu chính thống của kỳ thi.
- Lỗi và gap của chính học viên.
- Context của câu hỏi đang xem.

Kiến trúc RAG:

1. Ingest tài liệu được cấp quyền.
2. Chunk, gắn metadata certificate/skill/topic/version.
3. Tạo embedding và lưu vector index.
4. Retrieve theo câu hỏi và quyền người dùng.
5. Rerank context.
6. Sinh câu trả lời có citation nội bộ.
7. Từ chối nếu không đủ context.

Không để AI Tutor tự bịa quy định chấm thi hoặc đáp án nếu không có nguồn.

---

## 7. Mô hình dữ liệu đề xuất (MVP 16 bảng)

MVP ưu tiên triển khai nhanh, dữ liệu rõ ràng và dễ mở rộng. Không tạo hơn 50 bảng ngay từ đầu; các cấu trúc linh hoạt của AI được lưu JSON, còn trường dùng để tìm kiếm/thống kê giữ ở cột riêng.

### 7.1. Danh sách 16 bảng chính

- `users`
- `certificates`
- `topics`
- `questions`
- `rubrics`
- `exams`
- `exam_questions`
- `practice_packages`
- `exam_attempts`
- `attempt_answers`
- `attempt_events`
- `ai_evaluations`
- `ai_jobs`
- `gap_analyses`
- `recommendations`
- `learning_progress`

Chi tiết thiết kế:

| Nhóm | Bảng | Thiết kế gọn |
|---|---|---|
| Nền tảng | `users` | Tái sử dụng bảng user hiện có; role giữ trong `users.role` ở MVP |
| Nền tảng | `certificates` | IELTS/TOEIC/THPT, thang điểm, kỹ năng, điểm đạt |
| Nội dung | `topics` | Gộp subject/skill/topic/question type/difficulty bằng `type` và `parent_id` |
| Nội dung | `questions` | Đáp án, lựa chọn, media, explanation lưu JSON; có `status`, `version` |
| Nội dung | `rubrics` | Tiêu chí Writing/Speaking lưu trong `criteria_json` |
| Nội dung | `exams` | Blueprint và scoring rule lưu trong JSON; có workflow publish |
| Nội dung | `exam_questions` | Liên kết đề-câu hỏi, thứ tự, điểm riêng |
| Nội dung | `practice_packages` | Gói luyện theo topic, danh sách câu hỏi lưu JSON |
| Làm bài | `exam_attempts` | Timer, status, điểm tổng và breakdown JSON |
| Làm bài | `attempt_answers` | Câu trả lời, đúng/sai, điểm, thời gian từng câu |
| Làm bài | `attempt_events` | Autosave, tab switch, blur, copy/paste và anti-cheat event |
| AI | `ai_evaluations` | Điểm, criteria, lỗi, rewrite, confidence lưu JSON |
| AI | `ai_jobs` | Job async, retry, model, prompt version, token usage, cost |
| Phân tích | `gap_analyses` | Accuracy theo skill/topic/difficulty và weak topics JSON |
| Phân tích | `recommendations` | Gói luyện đề xuất, lý do, độ ưu tiên, trạng thái |
| Phân tích | `learning_progress` | Snapshot tiến bộ theo tuần/tháng và score trend JSON |

Các bảng quan trọng có `created_at`, `updated_at`, `created_by`, `updated_by`, `status`, `version` khi phù hợp. Có thể bổ sung `audit_logs` như bảng phụ cho login, publish, teacher override và retry AI.

### 7.2. Quy tắc gộp và mở rộng sau MVP

- `question_options`, `question_media`, `question_versions` gộp vào JSON/cột version của `questions`.
- `subjects`, `skills`, `topics`, `question_types` gộp vào `topics` với `type` và `parent_id`.
- `rubric_criteria`, `ai_evaluation_criteria`, `ai_feedback_errors` gộp vào JSON.
- `exam_versions`, `exam_sections`, `scoring_rules` gộp vào `exams.blueprint_json`, `scoring_json` và `exam_questions.section_name`.
- `score_results`, `score_breakdowns`, `teacher_overrides` gộp vào `exam_attempts` và `audit_logs`.
- `ai_prompts`, `ai_model_versions`, `ai_usage_logs` gộp vào `ai_jobs`.
- `learning_events`, `progress_snapshots`, `gap_metrics` gộp vào `attempt_events`, `gap_analyses`, `learning_progress`.

Chỉ tách thành bảng riêng khi cần query theo từng phần tử JSON, versioning/audit đầy đủ, báo cáo phức tạp hoặc tracking chi phí AI theo user/model. Việc tách phải thực hiện bằng migration và backfill, không phá dữ liệu attempt cũ.

### 7.3. Module hóa code bắt buộc

Code phải tổ chức theo **business module**, không gom tất cả entity/service/controller vào một package chung:

```text
adaptivelearning/
├── config/                 # security, database, web, exception, response
├── shared/                 # base entity, enums dùng chung, pagination, audit
├── module/
│   ├── users/              # auth, profile, user management
│   ├── certificates/       # certificate, skill/topic catalog
│   ├── questions/          # question bank, review, question metadata
│   ├── exams/              # exam blueprint, publish, exam-question
│   ├── attempts/           # timer, autosave, submit, scoring, anti-cheat
│   ├── ai/                 # AI gateway, jobs, prompts, provider adapters
│   ├── analytics/          # gap analysis, progress, recommendations
│   └── practice/           # practice packages and completion
└── infrastructure/         # persistence, external AI, storage, messaging
```

Mỗi module giữ cấu trúc nhất quán: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`. Controller chỉ nhận/trả DTO; business rule nằm ở service; repository chỉ truy cập dữ liệu; không để module gọi trực tiếp repository của module khác. Giao tiếp giữa module dùng service interface hoặc application event.

Nguyên tắc clean code:

- Tên class/method thể hiện nghiệp vụ, method ngắn và một trách nhiệm.
- Dùng request/response DTO, không expose entity trực tiếp qua API.
- Validate ở boundary và kiểm tra authorization trong service.
- Dùng enum/state transition thay cho string rải rác.
- Tách scoring deterministic khỏi AI grading.
- AI provider đi qua `AiGateway`, không gọi SDK trực tiếp từ controller.
- Job AI phải idempotent, retry có giới hạn, timeout và lưu trạng thái.
- Không hard-code secret, prompt, model hoặc database credential.
- Mỗi module có unit test cho service và integration test cho API chính.

---

## 8. API nghiệp vụ đề xuất

### Auth và profile

- `POST /api/v1/auth/signup`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/profile`
- `PATCH /api/v1/users/me`
- `POST /api/v1/users/me/change-password`

### Mục tiêu

- `GET /api/v1/certificates`
- `POST /api/v1/goals`
- `GET /api/v1/goals/me`
- `PATCH /api/v1/goals/{id}`
- `POST /api/v1/goals/{id}/diagnostic/start`

### Question bank và exam management

- `POST /api/v1/admin/questions`
- `PUT /api/v1/admin/questions/{id}`
- `POST /api/v1/admin/questions/{id}/submit-review`
- `POST /api/v1/admin/questions/{id}/approve`
- `POST /api/v1/admin/exams`
- `POST /api/v1/admin/exams/{id}/publish`
- `GET /api/v1/exams`
- `GET /api/v1/exams/{id}`

### Mock test

- `POST /api/v1/exams/{examId}/attempts`
- `GET /api/v1/attempts/{id}`
- `PATCH /api/v1/attempts/{id}/answers`
- `POST /api/v1/attempts/{id}/events`
- `POST /api/v1/attempts/{id}/violations`
- `POST /api/v1/attempts/{id}/submit`
- `GET /api/v1/attempts/{id}/result`
- `GET /api/v1/attempts/me/history`

### AI grading

- `POST /api/v1/attempts/{id}/ai-evaluations`
- `GET /api/v1/ai-evaluations/{id}`
- `POST /api/v1/ai-evaluations/{id}/retry`
- `POST /api/v1/ai-evaluations/{id}/teacher-review`

### Analysis và recommendation

- `GET /api/v1/analysis/attempts/{attemptId}`
- `GET /api/v1/analysis/me/overview`
- `GET /api/v1/progress/me`
- `GET /api/v1/recommendations/me`
- `POST /api/v1/recommendations/{id}/start`
- `POST /api/v1/practice-packages/{id}/complete`

### AI operations cho admin

- `POST /api/v1/admin/ai/questions/draft`
- `POST /api/v1/admin/ai/exams/blueprint`
- `GET /api/v1/admin/ai/jobs`
- `GET /api/v1/admin/ai/usage`
- `POST /api/v1/admin/ai/prompts/{id}/activate`

---

## 9. Kiến trúc kỹ thuật đề xuất

### Backend hiện tại

- Spring Boot 3.2, Java 17.
- Spring Security + JWT.
- MySQL.
- Spring Data JPA cho aggregate/domain chính.
- MyBatis cho query report phức tạp nếu cần.
- Gradle.

### Nên bổ sung

- Flyway hoặc Liquibase thay cho phụ thuộc hoàn toàn vào `ddl-auto: update`.
- Redis cho cache, rate limit, idempotency và trạng thái tạm.
- Message broker như RabbitMQ/Kafka cho AI jobs.
- Object storage S3/MinIO cho audio, ảnh và file bài làm.
- Vector database hoặc PostgreSQL pgvector cho RAG ở giai đoạn sau.
- Scheduler để auto-submit attempt và retry job.
- OpenTelemetry, metrics và centralized logging.

### AI Gateway

Tạo một abstraction riêng thay vì gọi trực tiếp provider trong controller:

```text
AiProvider
  -> ChatCompletionProvider
  -> SpeechToTextProvider
  -> EmbeddingProvider
  -> ModerationProvider
```

`AiOrchestrator` chịu trách nhiệm chọn model, prompt version, schema output, retry, timeout, fallback, quota và ghi usage log. Provider có thể thay đổi mà không ảnh hưởng nghiệp vụ.

### Xử lý bất đồng bộ

Các tác vụ sau phải chạy async:

- Chấm Writing/Speaking.
- Speech-to-text.
- Phân tích gap lớn.
- Sinh đề/câu hỏi.
- Tạo embedding.
- Regrade hàng loạt.

Job phải có `PENDING`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `RETRYING`, `CANCELLED`, idempotency key, retry limit, timeout và dead-letter handling.

---

## 10. Nguyên tắc AI an toàn và đáng tin cậy

- Structured output bắt buộc qua JSON Schema/DTO validation.
- Lưu prompt version, model version, input hash và raw response được bảo vệ.
- Không gửi password, JWT, thông tin thanh toán hoặc PII không cần thiết cho model.
- Ẩn danh bài làm khi có thể.
- Có cơ chế xóa audio, transcript và dữ liệu theo yêu cầu.
- Rate limit theo user và tenant.
- Kiểm tra prompt injection trong tài liệu và nội dung người dùng.
- Không dùng feedback AI làm quyết định kỷ luật duy nhất.
- Teacher có quyền review/override điểm AI.
- Hiển thị rõ feedback là AI-generated.
- Theo dõi hallucination, JSON failure, latency, cost và disagreement với giáo viên.
- Có fallback deterministic khi provider lỗi.

---

## 11. Trạng thái nghiệp vụ quan trọng

### Question

`DRAFT -> IN_REVIEW -> APPROVED -> PUBLISHED -> ARCHIVED`

### Exam

`DRAFT -> REVIEW -> APPROVED -> PUBLISHED -> CLOSED -> ARCHIVED`

### Attempt

`IN_PROGRESS -> SUBMITTED -> GRADING -> GRADED`

Nhánh đặc biệt: `IN_PROGRESS -> EXPIRED`, `SUBMITTED -> NEEDS_REVIEW`, `GRADING -> FAILED`.

### AI Job

`PENDING -> PROCESSING -> SUCCEEDED` hoặc `RETRYING -> FAILED`.

Mọi chuyển trạng thái không hợp lệ phải bị từ chối ở service layer, không chỉ dựa vào frontend.

---

## 12. Dashboard và báo cáo

### Student dashboard

- Điểm hiện tại và điểm mục tiêu.
- Khoảng cách còn thiếu theo kỹ năng.
- Xác suất đạt mục tiêu và ngày thi dự kiến.
- Việc cần làm hôm nay.
- Lỗi lặp lại nhiều nhất.
- Lịch sử điểm, accuracy, thời gian và streak.
- Gói luyện được ưu tiên.

### Teacher dashboard

- Câu hỏi có accuracy bất thường.
- Câu hỏi có nhiều người báo lỗi.
- Phân bố điểm theo đề.
- Gap phổ biến theo nhóm học viên.
- So sánh điểm AI và điểm teacher.
- Tỷ lệ AI job thành công và thời gian xử lý.

### Admin dashboard

- DAU/MAU, số attempt, completion rate.
- Chi phí AI theo provider/model/user.
- Token, latency, failure rate.
- Tỷ lệ teacher override.
- Nội dung đang chờ duyệt.
- Audit log và cảnh báo bảo mật.

---

## 13. Lộ trình triển khai

### Phase 0 - Làm sạch nền tảng

- Chuẩn hóa role và principal.
- Bỏ duplicate `UserDetailsService`.
- Externalize secret và database credentials.
- Thêm migration, validation password và test foundation.
- Chuẩn hóa error response, pagination và audit.

### Phase 1 - Question Bank và Exam

- Certificate, skill, topic, question, rubric.
- CRUD admin/editor.
- Versioning và workflow duyệt.
- Tạo blueprint và publish exam.

### Phase 2 - Mock Test Engine

- Attempt, snapshot đề, timer server-side.
- Autosave, submit, expire.
- Anti-cheat events.
- Auto scoring và result review.

### Phase 3 - AI Writing

- AI Gateway, prompt registry và provider adapter.
- Async grading job.
- JSON schema, retry, timeout, usage log.
- Feedback lỗi và rewrite suggestion.

### Phase 4 - Speaking và Gap Analysis

- Object storage, speech-to-text.
- Timestamp feedback.
- Gap metrics, progress dashboard.
- Rule-based recommendation.

### Phase 5 - AI nâng cao

- Sinh nội dung có kiểm duyệt.
- RAG AI Tutor.
- Adaptive test.
- Dự báo điểm và tối ưu learning path.

---

## 14. Tiêu chí nghiệm thu MVP

- User có thể đăng ký, đăng nhập và chọn mục tiêu.
- Admin tạo được câu hỏi, rubric, đề và publish version.
- Student bắt đầu attempt và không nhận được đáp án đúng.
- Refresh hoặc mất mạng ngắn hạn không làm mất đáp án đã autosave.
- Server tự khóa attempt khi hết giờ.
- Câu khách quan được chấm nhất quán và có explanation.
- Writing được tạo AI evaluation async, có trạng thái và retry.
- Feedback AI đúng schema, hiển thị lỗi có bằng chứng và confidence.
- Gap analysis chỉ ra ít nhất skill/topic/type yếu nhất.
- Recommendation có lý do, độ ưu tiên và mục tiêu đo được.
- Không lộ secret trong source hoặc log.
- Các action quan trọng có audit log.
- Có unit test cho scoring, state transition và recommendation rule.
- Có integration test cho login, start attempt, autosave, submit và grading.

---

## 15. Cách chạy môi trường local

```bash
# Khởi động MySQL
 docker compose up -d

# Chạy ứng dụng Spring Boot
 .\gradlew.bat bootRun

# Chạy test
 .\gradlew.bat test
```

Mặc định local dùng MySQL database `adaptivelearning` tại `localhost:3306`. Có thể ghi đè bằng `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`. Các secret AI phải đặt bằng environment variable hoặc secret manager, không commit vào repository.

---

## 16. Kết luận

Adaptive Learning AI nên được xây dựng như một hệ thống đánh giá năng lực có dữ liệu, không chỉ là ứng dụng sinh câu hỏi bằng LLM. Nền tảng khác biệt của dự án là chuỗi khép kín:

```text
Mục tiêu -> Diagnostic -> Mock Test -> Chấm điểm -> AI Feedback
-> Gap Analysis -> Recommendation -> Practice -> Đo lại -> Cập nhật lộ trình
```

Ưu tiên triển khai nền tảng chấm điểm và dữ liệu đáng tin cậy trước, sau đó mở rộng AI theo hướng có rubric, có version, có kiểm duyệt, có đo lường và có khả năng giải thích. Đây là cách tạo ra sản phẩm AI mạnh nhưng vẫn kiểm soát được chất lượng, chi phí và rủi ro.
