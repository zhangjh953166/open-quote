-- Drop the example actuarial databases and remove all access rights associated with them
drop database if exists ##dbname##_AIL_Base_DataSource_Master_Motor;
drop database if exists ##dbname##_AIL_Base_DataSource_Test;
drop database if exists ##dbname##_MetropolitanInsurance_Motor_Staging;
drop database if exists ##dbname##_StarInsurance_Motor_Staging;

revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'charlie'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'lily'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'oliver'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'ethan'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'sophie'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'thomas'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Master_Motor.* from 'report'@'localhost';

revoke all on ##dbname##_MetropolitanInsurance_Motor_Staging.* from 'charlie'@'localhost';
revoke all on ##dbname##_MetropolitanInsurance_Motor_Staging.* from 'lily'@'localhost';
revoke all on ##dbname##_MetropolitanInsurance_Motor_Staging.* from 'oliver'@'localhost';
revoke all on ##dbname##_MetropolitanInsurance_Motor_Staging.* from 'report'@'localhost';

revoke all on ##dbname##_StarInsurance_Motor_Staging.* from 'ethan'@'localhost';
revoke all on ##dbname##_StarInsurance_Motor_Staging.* from 'sophie'@'localhost';
revoke all on ##dbname##_StarInsurance_Motor_Staging.* from 'thomas'@'localhost';
revoke all on ##dbname##_StarInsurance_Motor_Staging.* from 'report'@'localhost';

revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'adam'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'charlie'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'lily'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'oliver'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'ethan'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'sophie'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'thomas'@'localhost';
revoke all on ##dbname##_AIL_Base_DataSource_Test.* from 'report'@'localhost';
