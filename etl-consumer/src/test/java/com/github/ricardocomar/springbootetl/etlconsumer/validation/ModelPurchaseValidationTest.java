package com.github.ricardocomar.springbootetl.etlconsumer.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
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
	private Purchase purchase;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates(PurchaseModelFixture.class.getPackage().getName());
	}

	@Before
	public void before() {
		purchase = Fixture.from(Purchase.class).gimme("valid");
	}

	@Test
	public void testValid() {
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyTransaction() throws Exception {
		purchase.setTransaction(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testInvalidTransaction() throws Exception {
		purchase.setTransaction("ABC");
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyId() throws Exception {
		purchase.setId(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyCustomer() throws Exception {
		purchase.setCustomer(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyStatus() throws Exception {
		purchase.setStatus(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyDate() throws Exception {
		purchase.setDate(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testInvalidDate() throws Exception {
		purchase.setDate(LocalDate.now().plusDays(1));
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testNullItems() throws Exception {
		purchase.setItems(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyItems() throws Exception {
		purchase.setItems(new ArrayList<PurchaseItem>());
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testOverloadedItems() throws Exception {
		purchase.setItems(Fixture.from(PurchaseItem.class).gimme(6, "g6play"));
		purchase.setTotalValue(
				purchase.getItems().stream().map(PurchaseItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add));
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testEmptyTotalValue() throws Exception {
		purchase.setTotalValue(null);
		validator.validate(purchase);
	}

	@Test(expected = ValidationException.class)
	public void testInvalidTotalValue() throws Exception {
		purchase.setTotalValue(new BigDecimal(1.0));
		validator.validate(purchase);
	}
}
