数据库为dzh,schema为PP
--支付记录表添加字段，银行卡分期
ALTER TABLE PP.TBL_PAYMENT_PAY_RECORD ADD CFL_COUNT INTEGER;
ALTER TABLE PP.TBL_PAYMENT_PAY_RECORD ADD CFL_RATE DECIMAL(10,6);
ALTER TABLE PP.TBL_PAYMENT_PAY_RECORD ADD MERCHANT_FEE_SUBSIDY DECIMAL(10,6);
ALTER TABLE PP.TBL_PAYMENT_PAY_RECORD ADD MERCHANT_AMOUNT_SUBSIDY DECIMAL(10,6);