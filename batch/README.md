Spring Batch는 배치 작업을 효과적으로 관리하고 추적하기 위해 다양한 메타데이터 테이블을 생성합니다. 이 테이블들은 배치 작업(Job)과 단계(Step)의 상태, 실행 이력 등을 저장하는 데 사용됩니다. Spring Batch는 `JobRepository`를 통해 이러한 테이블을 사용합니다. 주요 테이블들을 설명하겠습니다.

### 주요 테이블

1. **BATCH_JOB_INSTANCE**
    - **설명**: 배치 작업 인스턴스를 저장하는 테이블입니다. 각 배치 작업의 고유한 인스턴스에 대한 정보를 포함합니다.
    - **주요 컬럼**:
        - `JOB_INSTANCE_ID`: 작업 인스턴스의 고유 ID
        - `JOB_NAME`: 작업 이름
        - `JOB_KEY`: 작업 인스턴스의 고유 키

2. **BATCH_JOB_EXECUTION**
    - **설명**: 배치 작업 실행에 대한 정보를 저장합니다. 작업 인스턴스의 각 실행에 대한 상태와 결과를 기록합니다.
    - **주요 컬럼**:
        - `JOB_EXECUTION_ID`: 작업 실행의 고유 ID
        - `JOB_INSTANCE_ID`: 관련된 작업 인스턴스의 ID
        - `START_TIME`: 작업 실행 시작 시간
        - `END_TIME`: 작업 실행 종료 시간
        - `STATUS`: 작업 실행 상태 (예: COMPLETED, FAILED)
        - `EXIT_CODE`: 작업 실행 종료 코드
        - `EXIT_MESSAGE`: 작업 실행 종료 메시지

3. **BATCH_JOB_EXECUTION_PARAMS**
    - **설명**: 배치 작업 실행 시 사용된 파라미터를 저장합니다.
    - **주요 컬럼**:
        - `JOB_EXECUTION_ID`: 관련된 작업 실행의 ID
        - `TYPE_CD`: 파라미터 타입 (예: STRING, DATE)
        - `KEY_NAME`: 파라미터 키
        - `STRING_VAL`, `DATE_VAL`, `LONG_VAL`, `DOUBLE_VAL`: 파라미터 값

4. **BATCH_STEP_EXECUTION**
    - **설명**: 배치 작업의 각 단계 실행에 대한 정보를 저장합니다. 단계의 실행 상태와 결과를 기록합니다.
    - **주요 컬럼**:
        - `STEP_EXECUTION_ID`: 단계 실행의 고유 ID
        - `JOB_EXECUTION_ID`: 관련된 작업 실행의 ID
        - `STEP_NAME`: 단계 이름
        - `START_TIME`: 단계 실행 시작 시간
        - `END_TIME`: 단계 실행 종료 시간
        - `STATUS`: 단계 실행 상태 (예: COMPLETED, FAILED)
        - `READ_COUNT`, `WRITE_COUNT`, `COMMIT_COUNT`, `ROLLBACK_COUNT`: 단계의 주요 메트릭
        - `EXIT_CODE`: 단계 실행 종료 코드
        - `EXIT_MESSAGE`: 단계 실행 종료 메시지

5. **BATCH_STEP_EXECUTION_CONTEXT**
    - **설명**: 배치 단계 실행 시의 컨텍스트 정보를 저장합니다.
    - **주요 컬럼**:
        - `STEP_EXECUTION_ID`: 관련된 단계 실행의 ID
        - `SHORT_CONTEXT`: 짧은 컨텍스트 정보
        - `SERIALIZED_CONTEXT`: 직렬화된 컨텍스트 정보

6. **BATCH_JOB_EXECUTION_CONTEXT**
    - **설명**: 배치 작업 실행 시의 컨텍스트 정보를 저장합니다.
    - **주요 컬럼**:
        - `JOB_EXECUTION_ID`: 관련된 작업 실행의 ID
        - `SHORT_CONTEXT`: 짧은 컨텍스트 정보
        - `SERIALIZED_CONTEXT`: 직렬화된 컨텍스트 정보

7. **BATCH_STEP_EXECUTION_SEQ**
    - **설명**: 단계 실행 ID의 시퀀스를 저장합니다.
    - **주요 컬럼**:
        - `ID`: 단계 실행 ID의 시퀀스 값

8. **BATCH_JOB_EXECUTION_SEQ**
    - **설명**: 작업 실행 ID의 시퀀스를 저장합니다.
    - **주요 컬럼**:
        - `ID`: 작업 실행 ID의 시퀀스 값

### 테이블 관계
이 테이블들은 서로 연관 관계를 가집니다. 예를 들어, `BATCH_JOB_INSTANCE`는 여러 개의 `BATCH_JOB_EXECUTION`과 관계를 맺고, 각 `BATCH_JOB_EXECUTION`은 여러 개의 `BATCH_STEP_EXECUTION`과 관계를 맺습니다. 이러한 관계를 통해 배치 작업의 전체 실행 이력을 추적할 수 있습니다.

### 요약
Spring Batch는 배치 작업과 단계의 실행을 효율적으로 관리하기 위해 다양한 메타데이터 테이블을 사용합니다. 각 테이블은 배치 작업의 인스턴스, 실행 이력, 실행 파라미터, 단계 실행, 실행 컨텍스트 등의 정보를 저장합니다. 이러한 테이블을 통해 배치 작업의 상태를 모니터링하고, 문제 발생 시 디버깅할 수 있습니다.