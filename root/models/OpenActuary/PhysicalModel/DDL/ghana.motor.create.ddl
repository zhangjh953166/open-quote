CREATE TABLE datDataset (datID int(10) NOT NULL UNIQUE, datLoadDetailsIDloa int(10) NOT NULL);
CREATE TABLE orgOrganistaion (orgID int(10) NOT NULL UNIQUE, orgName varchar(100) NOT NULL UNIQUE, orgLoadDetailsIDloa int(10) NOT NULL);
CREATE TABLE repRepleacable (repID int(10) NOT NULL UNIQUE, repReplaced date NOT NULL);
CREATE TABLE ideIdentifiable (ideID int(10) NOT NULL UNIQUE, ideClientID varchar(100) NOT NULL, ideRecordID varchar(100) NOT NULL, ideSystemID varchar(100) NOT NULL);
CREATE TABLE loaLoadDetails (loaID int(10) NOT NULL UNIQUE, loaDescription varchar(255) NOT NULL, loaLoadID varchar(64) NOT NULL UNIQUE, loaLoadedBy varchar(50) NOT NULL, loaLoadedOn date NOT NULL);
CREATE TABLE occENOccupation (name varchar(32) NOT NULL UNIQUE);
CREATE TABLE natENNationality (name varchar(32) NOT NULL UNIQUE);
CREATE TABLE empENEmployment (name varchar(32) NOT NULL UNIQUE);
CREATE TABLE couENCountry (name varchar(32) NOT NULL, iso varchar(3) NOT NULL UNIQUE);
CREATE TABLE comCompany (comID int(11) NOT NULL UNIQUE, comHeadOfficeIDadd int(11) NOT NULL);
CREATE TABLE perPerson (perID int(11) NOT NULL UNIQUE, perResidenceIDadd int(11) NOT NULL, perEmploymentIDemp varchar(32) NOT NULL, perNationalityIDnat varchar(32) NOT NULL, perOccupationIDocc varchar(32) NOT NULL, perDateOfBirth date, perDefectiveHearing bit(1), perDefectiveVision bit(1), perGender varchar(6), perLastCriminalOffence date, perLastDrivingOffence date, perLastMotorRelatedAccident date, perLastMotorRelatedClaim date, perMotorInsuranceCancelledByInsurer bit(1), perMotorInsuranceConditionsAdded bit(1), perMotorInsuranceRefusedOnRenewal bit(1), perNumberOfCriminalOffences int(2), perNumberOfDrivingOffenceConvictions int(2), perNumberOfMotorRelatedAccidents int(2), perNumberOfMotorRelatedClaims int(2), perPreviouslyDeclineMotorInsurance bit(1), perPreviousNCD bit(1), perRequiredIncreasePremium bit(1), perSufferedFits bit(1));
CREATE TABLE driDriver (driID int(10) NOT NULL UNIQUE, driIDper int(11) NOT NULL, driAverageAnnualMilage int(10) DEFAULT 0 NOT NULL, driLicenceFirstIssued date, driLicenceNumber varchar(25), driMainDriver bit(1), driPassedDrivingTest date, driVehicleOwner bit(1), driYearsLicenceHeld int(2), driLicencedIssuedIDcou varchar(3) NOT NULL);
CREATE TABLE accAccident (accID int(11) NOT NULL UNIQUE, accCarryingGoods bit(1), accCauseOfAccident text, accDamageToThirdParty bit(1), accDriverAirbagDeployed bit(1), addDriverAtTimeOfAccidentIDdri int(10) NOT NULL, accDriverLiable bit(1), accInjuryToDriver bit(1), accInjuryOtherVehicleDrivers bit(1), accInjuryToOtherVehiclePassengers bit(1), accInjuryToThers bit(1), accInjuryToPassengers bit(1), accInjuryToThirdParties bit(1), accInsuredDriving bit(1), accLightsOn bit(1), accLocationInRoad text, accNamedDriverFromListOfInsured bit(1), accNumberOfVehiclesInvolved int(3), accPassengerAirbagDeployed bit(1), accPoliceProsecutingDriver bit(1), accPoliceRecorded bit(1), accPoliceWitness bit(1));
CREATE TABLE theTheftFire (theID int(11) NOT NULL UNIQUE, theIDcla int(11) NOT NULL, theAlarmOnAtTimeOfIncident bit(1), theAllWindowsAndDoorsSecured bit(1), theAnyToolsInVehicleAtTheTimeOfIncident bit(1), theKeysInVehicle bit(1), theKeysStolenWithVehicle bit(1), theLockedInGarage bit(1));
CREATE TABLE addAddress (addID int(11) NOT NULL UNIQUE, addName varchar(100), addNumber varchar(20), addLine1 varchar(100), addLine2 varchar(100), addLine3 varchar(100), addPostalAreaCode varchar(50) NOT NULL comment 'Either Postal code or area');
CREATE TABLE claENClaimType (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE claClaim (claID int(11) NOT NULL UNIQUE, polPolicyIDcla int(11) NOT NULL, claIDveh int(11) NOT NULL, claIDcla varchar(32) NOT NULL, claAmountPaidAmount decimal(12, 2), claAmountPaidIDccy varchar(3) NOT NULL, claLegalFeesAmount decimal(12, 2), claLegalFeesIDccy varchar(3) NOT NULL, claRecoveriesAmount decimal(12, 2), claRecoveriesIDccy varchar(3) NOT NULL, claTotalLossIncuredAmount decimal(12, 2), claTotalLossIncurredIDccy varchar(3) NOT NULL, claIncidentAddressIDadd int(11) NOT NULL, claIncidentMileageAtTime int(10), claIncidentOccured date, theTheftFiretheID int(11), claIDacc int(11) NOT NULL) comment='Claim Details';
CREATE TABLE weiENWeightType (name varchar(3) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE risENRiskCode (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE mdfENModifications (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE accENAccessories (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE parENParked (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE souENSoundSystem (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE ccyENCurrency (iso varchar(3) NOT NULL UNIQUE, description int(11) NOT NULL, symbol char(3));
CREATE TABLE reaENReason (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE insENInsuredType (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE modENModel (name varchar(32) NOT NULL, modID int(11) NOT NULL UNIQUE);
CREATE TABLE cascadeMakeModel (makID int(11) NOT NULL, modlID int(11) NOT NULL, validFrom date, validTo date);
CREATE TABLE makENMake (name varchar(32) NOT NULL UNIQUE, makID int(11) NOT NULL UNIQUE);
CREATE TABLE bodENBodyType (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE vehVehicle (vehID int(11) NOT NULL UNIQUE, polPolicypolID int(11) NOT NULL, vehIDmak varchar(32) NOT NULL, vehIDmod int(11) NOT NULL, vehIDbod varchar(32) NOT NULL, vehIDsou varchar(32) NOT NULL, vehIDpar varchar(32) NOT NULL, vehIDacc varchar(32) NOT NULL, vehIDmdf varchar(32) NOT NULL, vehIDrsk varchar(32) NOT NULL, vehAlarmFitted bit(1) NOT NULL, vehBoughtFromNew bit(1) NOT NULL, vehYearOfManufacture int(4) NOT NULL, vehRoadWorthCertificate int(10) NOT NULL, vehRoadWorthyCertificateExpiry date NOT NULL, vehAgeOfVehicle int(3), vehCapactityVolume decimal(10, 3), vehColour varchar(100), vehCommercialVehicle bit(1), vehCommercialVehicleLicence varchar(50), vehCompanyVehicle bit(1), vehCoverStart date NOT NULL, vehCoverEnd date, vehDriverAirBagFitted bit(1), vehEngineSizeCC int(5), vehMilage int(7), vehPassengerAirbagFitted bit(1), vehRentalVehicle bit(1), vehRighthandDrive bit(1), vehRoadWorthyCertificate bit(1), vehRoadWorthyCertificateExpiry2 date, vehSeatBeltsUsed bit(1), vehSeatingCapacity int(3), vehSubjectToLoan bit(1), vehToolsStroredDayTime bit(1), vehToolsStroredNightTime bit(1), vehTrackingSystemFitted bit(1), vehTrailer bit(1), vehUsageBusiness bit(1), vehUsageCarriageOfGoods bit(1), vehUsageSocialHome bit(1), vehWhenIsuredTookPossession date, vehYearOfManufacture2 varchar(4), vehSumInsuredLocalAmount decimal(15, 2), vehSumInsuredLocalIDccy varchar(3) NOT NULL, vehSumInsuredForeignAmount decimal(15, 2), vehSumInsuredForeignIDccy varchar(3) NOT NULL, vehRiskCurrencyAmount decimal(15, 2), vehRiskCurrencyIDccy varchar(3) NOT NULL, vehPremiumForeignAmount decimal(15, 2), vehPremiumForeignIDccy varchar(3) NOT NULL, vehCurrentEstimateedValueAmount decimal(15, 2), vehCurrentEstimatedValueIDccy varchar(3) NOT NULL, vehPremiumLocalAmount decimal(15, 2), vehPremiumLocalIDccy varchar(3) NOT NULL, vehValueWhenNewAmount decimal(15, 2), vehValueWhenNewIDccy varchar(3) NOT NULL, vehUnladedWeightAmount decimal(10, 2), vehUnladedWeightIDwei varchar(3) NOT NULL, vehMaxLoadWeightAmount decimal(10, 2), vehMaxLoadWeightIDwei varchar(3) NOT NULL, vehNightTimeLocationIDacc int(11) NOT NULL, vehDayTimeLocationIDacc int(11) NOT NULL, polIDdri int(10) NOT NULL);
CREATE TABLE covENCover (name varchar(32) NOT NULL UNIQUE, validFrom date, validTo date);
CREATE TABLE polPolicy (polID int(11) NOT NULL UNIQUE, polIDcov varchar(32) NOT NULL, polIDins varchar(32) NOT NULL, polIDrea varchar(32) NOT NULL, polInception date NOT NULL, polExpiry date NOT NULL, polLeadInsurer bit(1) NOT NULL, polRenewal bit(1) NOT NULL, polNumber varchar(20) NOT NULL, polCoInsuranceCover decimal(9, 4), polEndorsement bit(1), polFacultativeCover decimal(9, 4), polQuotaShare decimal(9, 4), polTax decimal(9, 4), polUWYear int(4), polGrossPremiumAmount decimal(9, 2), polGrossPremiumIDccy varchar(3) NOT NULL, polDriverIDdri int(10) NOT NULL, polInsuredCompanyIDcom int(11) NOT NULL, polLoadDetailsIDloa int(10) NOT NULL, polIdentifyableIDide int(10) NOT NULL, polReplaceableIDrep int(10) NOT NULL);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle805580 (vehUnladedWeightIDwei), ADD CONSTRAINT FKvehVehicle805580 FOREIGN KEY (vehUnladedWeightIDwei) REFERENCES weiENWeightType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle434686 (vehMaxLoadWeightIDwei), ADD CONSTRAINT FKvehVehicle434686 FOREIGN KEY (vehMaxLoadWeightIDwei) REFERENCES weiENWeightType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle513914 (vehNightTimeLocationIDacc), ADD CONSTRAINT FKvehVehicle513914 FOREIGN KEY (vehNightTimeLocationIDacc) REFERENCES addAddress (addID);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle483056 (vehDayTimeLocationIDacc), ADD CONSTRAINT FKvehVehicle483056 FOREIGN KEY (vehDayTimeLocationIDacc) REFERENCES addAddress (addID);
ALTER TABLE claClaim ADD INDEX FKclaClaim863009 (polPolicyIDcla), ADD CONSTRAINT FKclaClaim863009 FOREIGN KEY (polPolicyIDcla) REFERENCES polPolicy (polID);
ALTER TABLE claClaim ADD INDEX FKclaClaim593125 (claIDveh), ADD CONSTRAINT FKclaClaim593125 FOREIGN KEY (claIDveh) REFERENCES vehVehicle (vehID);
ALTER TABLE claClaim ADD INDEX FKclaClaim899742 (claIDcla), ADD CONSTRAINT FKclaClaim899742 FOREIGN KEY (claIDcla) REFERENCES claENClaimType (name);
ALTER TABLE claClaim ADD INDEX FKclaClaim577868 (claAmountPaidIDccy), ADD CONSTRAINT FKclaClaim577868 FOREIGN KEY (claAmountPaidIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE claClaim ADD INDEX FKclaClaim198561 (claLegalFeesIDccy), ADD CONSTRAINT FKclaClaim198561 FOREIGN KEY (claLegalFeesIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE claClaim ADD INDEX FKclaClaim84070 (claRecoveriesIDccy), ADD CONSTRAINT FKclaClaim84070 FOREIGN KEY (claRecoveriesIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE claClaim ADD INDEX FKclaClaim433842 (claTotalLossIncurredIDccy), ADD CONSTRAINT FKclaClaim433842 FOREIGN KEY (claTotalLossIncurredIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE claClaim ADD INDEX FKclaClaim81114 (claIncidentAddressIDadd), ADD CONSTRAINT FKclaClaim81114 FOREIGN KEY (claIncidentAddressIDadd) REFERENCES addAddress (addID);
ALTER TABLE theTheftFire ADD INDEX FKtheTheftFi95248 (theIDcla), ADD CONSTRAINT FKtheTheftFi95248 FOREIGN KEY (theIDcla) REFERENCES claClaim (claID);
ALTER TABLE claClaim ADD INDEX FKclaClaim260565 (claIDacc), ADD CONSTRAINT FKclaClaim260565 FOREIGN KEY (claIDacc) REFERENCES accAccident (accID);
ALTER TABLE driDriver ADD INDEX FKdriDriver796676 (driIDper), ADD CONSTRAINT FKdriDriver796676 FOREIGN KEY (driIDper) REFERENCES perPerson (perID);
ALTER TABLE perPerson ADD INDEX FKperPerson672654 (perResidenceIDadd), ADD CONSTRAINT FKperPerson672654 FOREIGN KEY (perResidenceIDadd) REFERENCES addAddress (addID);
ALTER TABLE comCompany ADD INDEX FKcomCompany455877 (comHeadOfficeIDadd), ADD CONSTRAINT FKcomCompany455877 FOREIGN KEY (comHeadOfficeIDadd) REFERENCES addAddress (addID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy716061 (polDriverIDdri), ADD CONSTRAINT FKpolPolicy716061 FOREIGN KEY (polDriverIDdri) REFERENCES driDriver (driID);
ALTER TABLE driDriver ADD INDEX FKdriDriver227890 (driLicencedIssuedIDcou), ADD CONSTRAINT FKdriDriver227890 FOREIGN KEY (driLicencedIssuedIDcou) REFERENCES couENCountry (iso);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy484391 (polInsuredCompanyIDcom), ADD CONSTRAINT FKpolPolicy484391 FOREIGN KEY (polInsuredCompanyIDcom) REFERENCES comCompany (comID);
ALTER TABLE accAccident ADD INDEX FKaccAcciden279153 (addDriverAtTimeOfAccidentIDdri), ADD CONSTRAINT FKaccAcciden279153 FOREIGN KEY (addDriverAtTimeOfAccidentIDdri) REFERENCES driDriver (driID);
ALTER TABLE perPerson ADD INDEX FKperPerson450859 (perEmploymentIDemp), ADD CONSTRAINT FKperPerson450859 FOREIGN KEY (perEmploymentIDemp) REFERENCES empENEmployment (name);
ALTER TABLE perPerson ADD INDEX FKperPerson326206 (perNationalityIDnat), ADD CONSTRAINT FKperPerson326206 FOREIGN KEY (perNationalityIDnat) REFERENCES natENNationality (name);
ALTER TABLE perPerson ADD INDEX FKperPerson597755 (perOccupationIDocc), ADD CONSTRAINT FKperPerson597755 FOREIGN KEY (perOccupationIDocc) REFERENCES occENOccupation (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy909576 (polLoadDetailsIDloa), ADD CONSTRAINT FKpolPolicy909576 FOREIGN KEY (polLoadDetailsIDloa) REFERENCES loaLoadDetails (loaID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy563852 (polIdentifyableIDide), ADD CONSTRAINT FKpolPolicy563852 FOREIGN KEY (polIdentifyableIDide) REFERENCES ideIdentifiable (ideID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy616371 (polReplaceableIDrep), ADD CONSTRAINT FKpolPolicy616371 FOREIGN KEY (polReplaceableIDrep) REFERENCES repRepleacable (repID);
ALTER TABLE datDataset ADD INDEX FKdatDataset558285 (datLoadDetailsIDloa), ADD CONSTRAINT FKdatDataset558285 FOREIGN KEY (datLoadDetailsIDloa) REFERENCES loaLoadDetails (loaID);
ALTER TABLE orgOrganistaion ADD INDEX FKorgOrganis752643 (orgLoadDetailsIDloa), ADD CONSTRAINT FKorgOrganis752643 FOREIGN KEY (orgLoadDetailsIDloa) REFERENCES loaLoadDetails (loaID);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle975646 (vehIDsou), ADD CONSTRAINT FKvehVehicle975646 FOREIGN KEY (vehIDsou) REFERENCES souENSoundSystem (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle876353 (vehIDpar), ADD CONSTRAINT FKvehVehicle876353 FOREIGN KEY (vehIDpar) REFERENCES parENParked (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle886475 (vehIDacc), ADD CONSTRAINT FKvehVehicle886475 FOREIGN KEY (vehIDacc) REFERENCES accENAccessories (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle962787 (vehIDmdf), ADD CONSTRAINT FKvehVehicle962787 FOREIGN KEY (vehIDmdf) REFERENCES mdfENModifications (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle435925 (vehIDrsk), ADD CONSTRAINT FKvehVehicle435925 FOREIGN KEY (vehIDrsk) REFERENCES risENRiskCode (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle553259 (vehSumInsuredLocalIDccy), ADD CONSTRAINT FKvehVehicle553259 FOREIGN KEY (vehSumInsuredLocalIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle695315 (vehSumInsuredForeignIDccy), ADD CONSTRAINT FKvehVehicle695315 FOREIGN KEY (vehSumInsuredForeignIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle628009 (vehRiskCurrencyIDccy), ADD CONSTRAINT FKvehVehicle628009 FOREIGN KEY (vehRiskCurrencyIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle924719 (vehPremiumForeignIDccy), ADD CONSTRAINT FKvehVehicle924719 FOREIGN KEY (vehPremiumForeignIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle708351 (vehCurrentEstimatedValueIDccy), ADD CONSTRAINT FKvehVehicle708351 FOREIGN KEY (vehCurrentEstimatedValueIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle923862 (vehPremiumLocalIDccy), ADD CONSTRAINT FKvehVehicle923862 FOREIGN KEY (vehPremiumLocalIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle355497 (vehValueWhenNewIDccy), ADD CONSTRAINT FKvehVehicle355497 FOREIGN KEY (vehValueWhenNewIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy415247 (polIDins), ADD CONSTRAINT FKpolPolicy415247 FOREIGN KEY (polIDins) REFERENCES insENInsuredType (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy164033 (polIDrea), ADD CONSTRAINT FKpolPolicy164033 FOREIGN KEY (polIDrea) REFERENCES reaENReason (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy985785 (polGrossPremiumIDccy), ADD CONSTRAINT FKpolPolicy985785 FOREIGN KEY (polGrossPremiumIDccy) REFERENCES ccyENCurrency (iso);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle392086 (polPolicypolID), ADD CONSTRAINT FKvehVehicle392086 FOREIGN KEY (polPolicypolID) REFERENCES polPolicy (polID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy907769 (polIDcov), ADD CONSTRAINT FKpolPolicy907769 FOREIGN KEY (polIDcov) REFERENCES covENCover (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle766678 (vehIDbod), ADD CONSTRAINT FKvehVehicle766678 FOREIGN KEY (vehIDbod) REFERENCES bodENBodyType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle769412 (vehIDmod), ADD CONSTRAINT FKvehVehicle769412 FOREIGN KEY (vehIDmod) REFERENCES modENModel (modID);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle764335 (vehIDmak), ADD CONSTRAINT FKvehVehicle764335 FOREIGN KEY (vehIDmak) REFERENCES makENMake (name);
ALTER TABLE cascadeMakeModel ADD INDEX FKcascadeMak336466 (makID), ADD CONSTRAINT FKcascadeMak336466 FOREIGN KEY (makID) REFERENCES makENMake (makID);
ALTER TABLE cascadeMakeModel ADD INDEX FKcascadeMak936409 (modlID), ADD CONSTRAINT FKcascadeMak936409 FOREIGN KEY (modlID) REFERENCES modENModel (modID);

