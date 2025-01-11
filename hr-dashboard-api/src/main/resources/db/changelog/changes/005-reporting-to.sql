ALTER table "user"
    ADD COLUMN reporting_to INT NOT NULL DEFAULT 0;

CREATE INDEX IF NOT EXISTS idx_user_reporting_to ON "user"(reporting_to, status);