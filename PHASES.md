# Adaptive Learning AI - Trạng thái và Giai đoạn triển khai

Tài liệu này là bảng điều khiển tiến độ duy nhất của dự án. Mỗi khi hoàn thành một nhóm công việc, cập nhật checkbox, trạng thái và ngày kiểm tra tại đây.

**Ngày đánh giá hiện tại:** 2026-09-29  
**Giai đoạn hiện tại:** Phase 1 - Foundation và API MVP  
**Trạng thái tổng thể:** Đã hoàn thành phần lớn backend API, chưa hoàn thành kiểm thử tích hợp và nghiệm thu end-to-end  
**Mức hoàn thành ước tính:** 55-60%

> Lưu ý: API đã compile thành công không đồng nghĩa toàn bộ nghiệp vụ đã được nghiệm thu. Các phase sau cần test với dữ liệu thật, frontend và AI provider thật.

---

## 1. Tóm tắt trạng thái hiện tại

| Hạng mục | Trạng thái | Bằng chứng / ghi chú |
|---|---|---|
| Spring Boot foundation | DONE | App compile được với Gradle |
| Docker MySQL | DONE | Container `adaptive-learning-mysql` đã tạo |
| Auth/JWT/User | DONE | Đăng ký, đăng nhập, profile, role |
| Module nghiệp vụ nền tảng | DONE | Certificate, Topics, Questions, Exams |
| Mock Test Engine | IMPLEMENTED | Timer, autosave, submit, auto-score, violation |
| AI Writing grading | IMPLEMENTED | AiProvider, rubric, async job, evaluation |
| Gap Analysis | PARTIAL | Có API/entity/service; cần kiểm tra dữ liệu theo topic/skill thực tế |
| Practice package | IMPLEMENTED | CRUD package và question IDs JSON |
| AI Tutor | IMPLEMENTED MVP | Hội thoại AI theo context |
| Content Generation | IMPLEMENTED MVP | Sinh câu hỏi JSON, cần human review |
| Speaking | IMPLEMENTED MVP | Transcript grading; audio/STT chưa hoàn chỉnh |
| Adaptive Engine | IMPLEMENTED MVP | Heuristic profile/recommendation/forecast |
| Document RAG | IMPLEMENTED MVP | Chunk + keyword search; chưa dùng embedding/vector DB |
| Study Planner | IMPLEMENTED MVP | Tạo kế hoạch và task theo ngày |
| Unit tests | NOT STARTED | Chưa có file trong `src/test` |
| Integration tests | NOT STARTED | Chưa có test flow API với MySQL |
| Flyway/Liquibase | NOT STARTED | Hiện vẫn dùng `ddl-auto: update` |
| Frontend | NOT STARTED | Chưa có frontend trong repository |
| Production hardening | NOT STARTED | Cần xử lý secrets, CORS, rate limit, monitoring |

**Code hiện tại:** 15 business modules, khoảng 160 Java files, 16 shared enums.  
**Build gần nhất:** `./gradlew.bat clean compileJava` - `BUILD SUCCESSFUL`.

---

## 2. Phase 0 - Chuẩn bị nền tảng

**Trạng thái: DONE**

### Đã hoàn thành

- [x] Spring Boot 3.2 + Java 17 source compatibility.
- [x] Gradle build và dependency nền tảng.
- [x] Spring Security + JWT.
- [x] MySQL Docker Compose.
- [x] JPA/MyBatis setup.
- [x] Swagger/OpenAPI setup.
- [x] Global response và exception handler.
- [x] Cấu trúc code theo business module.
- [x] Shared enums và BaseEntity.

### Việc còn nên cải thiện

- [ ] Bỏ duplicate `UserDetailsServiceImpl`.
- [ ] Externalize JWT secret và database credentials.
- [ ] Đổi default credentials `admin/admin`, `user/user` khi chạy production.
- [ ] Thu hẹp CORS, không cho phép mọi origin khi production.
- [ ] Thêm `audit_logs` nếu cần truy vết publish/override/retry.

---

## 3. Phase 1 - Foundation và API MVP

**Trạng thái: CURRENT / CODE COMPLETE - QA CHƯA XONG**

Đây là phase dự án đang đứng: backend đã có các API chính nhưng cần kiểm tra nghiệp vụ thật.

### Module đã có

- [x] `users`: auth, JWT, profile.
- [x] `certificates`: chứng chỉ và thang điểm.
- [x] `topics`: cây subject/skill/topic.
- [x] `questions`: question bank và workflow duyệt.
- [x] `exams`: đề thi, gắn câu hỏi, publish.
- [x] `attempts`: start, timer, autosave, submit, scoring, anti-cheat.
- [x] `ai`: AI gateway, rubric, Writing grading, jobs.
- [x] `analytics`: gap analysis, progress, recommendations.
- [x] `practice`: practice packages.

### Việc bắt buộc phải làm để kết thúc Phase 1

- [ ] Login bằng admin và user qua Swagger.
- [ ] Tạo certificate IELTS mẫu.
- [ ] Tạo topics Reading/Writing/Speaking.
- [ ] Tạo và approve tối thiểu 20 câu hỏi.
- [ ] Tạo exam, gắn câu hỏi và publish.
- [ ] Chạy một attempt từ start → autosave → submit → result.
- [ ] Test hết giờ và auto-expire.
- [ ] Test vi phạm tab switch/copy/blur.
- [ ] Chạy gap analysis sau một attempt.
- [ ] Tạo practice package và kiểm tra recommendation.
- [ ] Viết test cho scoring và state transition.
- [ ] Thêm integration test cho login, exam, attempt và result.

### Điều kiện nghiệm thu Phase 1

- Người dùng không thể làm đề chưa `PUBLISHED`.
- Người dùng không thể xem đáp án đúng trước khi nộp.
- Server kiểm tra `expiresAt`, không tin timer phía client.
- Người dùng chỉ đọc/sửa được attempt của mình.
- Câu khách quan được chấm lặp lại cho cùng input ra cùng kết quả.
- Attempt lịch sử không bị thay đổi khi câu hỏi hiện tại được sửa.
- Lỗi API trả về response format thống nhất.

---

## 4. Phase 2 - AI Grading và AI Content

**Trạng thái: CODE MVP ĐÃ CÓ - CẦN KIỂM THỬ AI THẬT**

### Phạm vi

- Writing grading theo rubric.
- Speaking grading theo transcript.
- AI job, retry, timeout, idempotency.
- AI sinh câu hỏi và explanation.
- Teacher review nội dung AI sinh.
- Theo dõi confidence, token usage và cost.

### Việc cần làm tiếp theo

- [ ] Đặt `AI_API_KEY` qua environment variable/secret manager.
- [ ] Kiểm tra `AI_BASE_URL` và model đang dùng.
- [ ] Test prompt với 10 bài Writing thật.
- [ ] Test JSON output lỗi, markdown fence và missing fields.
- [ ] Thêm JSON Schema validation thay vì chỉ parse tự do.
- [ ] Đảm bảo job failed không làm mất evaluation cũ.
- [ ] Bổ sung endpoint teacher review/override có audit.
- [ ] Thêm giới hạn kích thước bài viết và rate limit AI.
- [ ] Không gửi JWT, password hoặc PII không cần thiết vào prompt.
- [ ] So sánh điểm AI với ít nhất 2-3 bài được giáo viên chấm.
- [ ] Thêm fallback deterministic khi AI provider lỗi.

### Điều kiện nghiệm thu Phase 2

- AI evaluation luôn có status rõ ràng: `PENDING/PROCESSING/SUCCEEDED/FAILED`.
- JSON lỗi được retry hoặc chuyển failed, không làm crash request.
- Có thể tra cứu evaluation theo attempt.
- Câu hỏi AI sinh ra luôn vào `DRAFT/IN_REVIEW`, không tự động public.
- Có thể kiểm tra model, prompt version và cost của từng job.

---

## 5. Phase 3 - AI Tutor, Speaking, RAG và Adaptive Learning

**Trạng thái: CODE MVP ĐÃ CÓ - ĐANG CHỜ E2E TEST**

### 5.1. AI Tutor - `tutor`

- [x] Conversation và message entity.
- [x] Lưu USER/ASSISTANT messages.
- [x] Prompt theo context và 10 messages gần nhất.
- [x] Gọi qua `AiProvider`, không gọi SDK trực tiếp từ controller.
- [ ] Thêm citations thật từ RAG search.
- [ ] Thêm giới hạn token/context.
- [ ] Thêm moderation/prompt injection protection.
- [ ] Test ownership conversation.

### 5.2. Content Generation - `contentgen`

- [x] Tạo generation request.
- [x] AI output JSON question array.
- [x] Validate trạng thái generation.
- [x] Apply bản nháp vào question workflow.
- [ ] Thêm kiểm tra duplicate question.
- [ ] Thêm kiểm tra một đáp án đúng duy nhất.
- [ ] Thêm teacher review UI/API đầy đủ.
- [ ] Lưu prompt/model/token usage chi tiết.

### 5.3. Speaking - `speaking`

- [x] Transcript submission.
- [x] AI grading theo prompt.
- [x] Criteria/errors/strengths/rewrite/confidence.
- [ ] Thêm upload audio qua S3/MinIO.
- [ ] Thêm Speech-to-Text provider.
- [ ] Lưu timestamp transcript.
- [ ] Phân tích pronunciation, fluency và pause.
- [ ] Kiểm tra file type, duration và kích thước audio.

### 5.4. Document RAG - `rag`

- [x] Upload text/Markdown.
- [x] Chunk khoảng 500 ký tự.
- [x] Keyword extraction và substring search.
- [x] Ownership check.
- [ ] Thêm PDF/DOCX extraction.
- [ ] Thêm embeddings.
- [ ] Thêm vector store (pgvector/Qdrant/Chroma).
- [ ] Rerank context trước khi gọi Tutor.
- [ ] Citations có document/chunk reference.
- [ ] Xóa dữ liệu theo document owner.

### 5.5. Adaptive Engine - `adaptive`

- [x] Learner profile theo certificate.
- [x] Ability by skill.
- [x] Cập nhật sau attempt.
- [x] Đề xuất difficulty heuristic.
- [x] Forecast điểm heuristic.
- [ ] Kết nối chính xác question → topic → skill.
- [ ] Không dùng `certificateId` hoặc skill giả định trong analytics.
- [ ] Version hóa adaptive model.
- [ ] Thêm fallback khi dữ liệu chưa đủ.
- [ ] Đánh giá forecast bằng dữ liệu lịch sử.
- [ ] Nâng cấp sang IRT/BKT nếu đủ dữ liệu.

### Điều kiện nghiệm thu Phase 3

- Tutor trả lời được câu hỏi về một câu sai và nêu được context.
- Speaking transcript được chấm và trả feedback có cấu trúc.
- RAG không trả tài liệu của user khác.
- Adaptive recommendation thay đổi khi ability thay đổi.
- Mọi AI output đều có model/version/status để debug.

---

## 6. Phase 4 - Study Planner và Progress Intelligence

**Trạng thái: CODE MVP ĐÃ CÓ - CẦN TÍCH HỢP VỚI GAP/ADAPTIVE**

### Phạm vi

- Study plan theo target score và exam date.
- Tự sinh task theo ngày.
- Task study/practice/mock test/review/writing/speaking.
- Cập nhật task hoàn thành.
- Progress summary.
- Tạo version kế hoạch khi năng lực thay đổi.

### Việc cần làm

- [ ] Sau gap analysis, planner ưu tiên weak topics.
- [ ] Sau adaptive update, planner điều chỉnh difficulty.
- [ ] Không tạo task vào ngày đã qua.
- [ ] Xử lý timezone rõ ràng.
- [ ] Thêm task overdue và reminder.
- [ ] Thêm endpoint regenerate plan có version mới.
- [ ] Đồng bộ completed task với attempt/practice thật.
- [ ] Test trường hợp exam date quá gần hoặc đã qua.

### Điều kiện nghiệm thu Phase 4

- Plan tạo được cho target score và exam date hợp lệ.
- Task phân bổ không vượt hoursPerWeek.
- Progress tính đúng từ task hoàn thành.
- Plan mới không phá lịch sử plan cũ.

---

## 7. Phase 5 - Frontend và Product Flow

**Trạng thái: NOT STARTED**

### Màn hình cần xây dựng

1. Login/Register.
2. Student onboarding và chọn goal.
3. Student dashboard.
4. Certificate/exam catalog.
5. Mock test runner với countdown.
6. Question navigation và autosave indicator.
7. Result/review answer.
8. AI Writing feedback.
9. Speaking transcript/audio feedback.
10. Gap analysis dashboard.
11. AI Tutor chat.
12. Document upload/RAG search.
13. Study planner calendar.
14. Adaptive recommendations.
15. Admin question bank.
16. Admin exam builder.
17. Admin AI generation review.
18. Admin AI jobs/cost dashboard.

### Việc cần làm

- [ ] Chọn frontend stack.
- [ ] Tạo API client và auth interceptor.
- [ ] Xử lý refresh/logout/token expiry.
- [ ] Hiển thị loading/error/empty state.
- [ ] Bảo vệ route theo role.
- [ ] Test responsive desktop/mobile.
- [ ] Test timer khi refresh/mất mạng.

---

## 8. Phase 6 - Quality, Security và Production

**Trạng thái: NOT STARTED**

### Testing

- [ ] Unit tests cho scoring engine.
- [ ] Unit tests cho state transitions.
- [ ] Unit tests cho gap thresholds.
- [ ] Unit tests cho adaptive recommendation.
- [ ] Unit tests cho planner scheduling.
- [ ] Integration tests với Testcontainers MySQL.
- [ ] Contract tests cho AI provider.
- [ ] Load test attempt autosave.
- [ ] Security test ownership và role access.

### Database và vận hành

- [ ] Thêm Flyway/Liquibase.
- [ ] Tắt `ddl-auto: update` ở production.
- [ ] Seed data chỉ qua migration/fixture.
- [ ] Backup/restore MySQL.
- [ ] Redis cho cache/rate limit nếu cần.
- [ ] Queue cho AI jobs nếu traffic tăng.
- [ ] Object storage cho audio/document.

### Security

- [ ] Secret manager cho JWT, DB, AI key.
- [ ] CORS allowlist.
- [ ] Rate limit login và AI endpoints.
- [ ] Validate file type, size và nội dung.
- [ ] Prompt injection protection.
- [ ] PII redaction trong log.
- [ ] Audit log cho admin actions.
- [ ] Data retention/xóa audio/document.

### Observability

- [ ] Structured logging.
- [ ] Correlation ID.
- [ ] Metrics latency/error/rate/token/cost.
- [ ] Health check DB/AI/storage.
- [ ] Alert cho AI failure rate và chi phí bất thường.

### Điều kiện nghiệm thu Phase 6

- Build/test chạy trong CI.
- Không có secret trong repository.
- Có migration reproducible trên database mới.
- Có test quyền sở hữu dữ liệu.
- Có log và metric đủ để điều tra lỗi AI.

---

## 9. Việc phải làm ngay bây giờ

Dự án đang ở **Phase 1 - API MVP đã code xong nhưng QA chưa hoàn tất**. Không nên tiếp tục mở thêm module trước khi hoàn thành checklist sau:

1. [ ] Khởi động Docker MySQL.
2. [ ] Khởi động app ở port 8080.
3. [ ] Mở Swagger: `http://localhost:8080/swagger-ui/index.html`.
4. [ ] Login admin và user.
5. [ ] Tạo certificate/topic/question/exam mẫu.
6. [ ] Chạy trọn một attempt.
7. [ ] Kiểm tra score, timer, autosave và anti-cheat.
8. [ ] Chạy gap analysis và recommendation.
9. [ ] Set `AI_API_KEY` và test Writing grading.
10. [ ] Ghi lỗi thực tế vào issue/checklist trước khi chuyển Phase 2.

### Tiêu chí chuyển sang phase tiếp theo

Chỉ đánh dấu Phase 1 DONE khi có:

- Một test scenario hoàn chỉnh chạy từ database sạch.
- API response được kiểm tra trên Swagger hoặc integration test.
- Không còn lỗi compile/startup.
- Có seed data lặp lại được.
- Có ít nhất unit test cho scoring.

---

## 10. Quy ước cập nhật file này

- `[x]` = đã kiểm chứng bằng code/test.
- `[ ]` = chưa làm.
- `IMPLEMENTED` = code có nhưng chưa chắc đã nghiệm thu end-to-end.
- `DONE` = code + test + nghiệp vụ đã xác nhận.
- `PARTIAL` = có một phần, còn thiếu integration hoặc dữ liệu thực.
- Sau mỗi phiên làm việc, cập nhật `Ngày đánh giá`, phase hiện tại và checklist.
- Không đánh dấu DONE chỉ vì `compileJava` thành công.
