create or replace procedure insert_table_summary()
    language plpgsql
as $$
begin
    -- body
    insert into jalin.table_summary(card_number,
                                    iss,
                                    acq,
                                    dest,
                                    status_jalin,
                                    status_iss,
                                    status_acq,
                                    status_dest)
    select tj.card_number,
           tj.iss,
           tj.acq,
           tj.dest,
           tj.status as status_jalin,
           (select tb.status from jalin.table_bank tb where tb.card_number = tj.card_number and tb.source = tj.iss) as status_iss,
           (select tb.status from jalin.table_bank tb where tb.card_number = tj.card_number and tb.source = tj.acq) as status_acq,
           (select tb.status from jalin.table_bank tb where tb.card_number = tj.card_number and tb.source = tj.dest) as status_dest
    from jalin.table_jalin tj ;

end;$$

-- call insert_table_summary();