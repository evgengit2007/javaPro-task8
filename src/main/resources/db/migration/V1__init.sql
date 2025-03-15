DROP TABLE IF EXISTS public.limits CASCADE;

CREATE TABLE public.limits
(id bigserial PRIMARY KEY,
userid bigint unique,
limit_summa real,
block_amount real
);
CREATE INDEX idx_limits_id ON public.limits(id);
