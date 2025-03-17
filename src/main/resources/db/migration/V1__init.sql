DROP TABLE IF EXISTS public.limits CASCADE;

CREATE TABLE public.limits
(id bigserial PRIMARY KEY,
userid bigint unique,
limit_summa real not null,
block_amount real not null
);
CREATE INDEX idx_limits_id ON public.limits(id);

insert into public.limits (userid, limit_summa, block_amount)
values
    (1, 10000.00, 0),
    (2, 10000.00, 0),
    (3, 10000.00, 0),
    (4, 10000.00, 0),
    (5, 10000.00, 0),
    (6, 10000.00, 0),
    (7, 10000.00, 0),
    (8, 10000.00, 0),
    (9, 10000.00, 0),
    (10, 10000.00, 0)
;
