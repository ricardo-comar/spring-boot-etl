package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ricardocomar.springbootetl.etlconsumer.fixture.PurchaseModelFixture;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.etlconsumer.model.PurchaseItem;

import br.com.fluentvalidator.exception.ValidationException;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValidationSpringConfig.class)
public class ModelPurchaseValidationTest {

	@Autowired
	private ValidatorPurchase validator;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(PurchaseModelFixture.class.getPackage().getName());
	}

	@Test
	public void testValid() {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");

		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyTransaction() throws Exception {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");
		purchase.setTransaction(null);

		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyName() throws Exception {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");
		purchase.setId(null);

		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyEmployees() throws Exception {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");

		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testOverloadedEmployees() throws Exception {

		final Purchase purchase = Fixture.from(Purchase.class).gimme("valid");
		purchase.setItems(Fixture.from(PurchaseItem.class).gimme(5, "g6play"));
		purchase.setTotalValue(
				purchase.getItems().stream().map(PurchaseItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));

		validator.validate(purchase);
	}
}
