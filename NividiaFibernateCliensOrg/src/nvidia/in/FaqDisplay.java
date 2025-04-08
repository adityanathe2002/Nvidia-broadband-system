package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FaqDisplay extends JFrame {

    private JPanel faqPanel;
    private JTextField searchField;
    private JScrollPane scrollPane;
    
    private String[][] faqs;

    public FaqDisplay() {
        setTitle("Nvidia Fibernet - FAQs");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(400, 30));
        searchField.setToolTipText("Search FAQs...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterFAQs(searchField.getText());
            }

            
        });
    

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        // FAQ Panel (Scrollable)
        faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        loadFAQs();

        scrollPane = new JScrollPane(faqPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(850, 500));

        // Add components
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // Method to load FAQ Data in Page Format
    private void loadFAQs() {
        faqs = new String[][] {
            {"1", "How can I view my current bill?", "➜ Log in to your Nvidia Fibernet account and go to the 'Billing' section."},
            {"2", "Where can I find my past billing statements?", "➜ You can download past bills from the 'Billing History' section."},
            {"3", "Can I receive my bill via email?", "➜ Yes, enable 'Paperless Billing' in your account settings."},
            {"4", "What happens if I don't pay my bill on time?", "➜ A late fee may be applied, and your service may be suspended."},
            {"5", "How do I update my billing address?", "➜ Go to 'Account Settings' and update your billing address."},
            {"6", "What payment methods are accepted?", "➜ We accept credit/debit cards, UPI, and net banking."},
            {"7", "Can I set up automatic payments?", "➜ Yes, enable auto-pay under 'Payment Settings'."},
            {"8", "How do I add or remove a payment method?", "➜ Go to 'Billing & Payments' and select 'Manage Payment Methods'."},
            {"9", "Can I make a one-time payment without logging in?", "➜ No, you must log in to make a payment."},
            {"10", "Are there any additional charges for using credit/debit cards?", "➜ No extra charges are applied for card payments."},
            {"11", "Is there a grace period for late bill payments?", "➜ Yes, a grace period of 7 days is provided before suspension."},
            {"12", "What are the late payment charges?", "➜ Late fees depend on your plan, ranging from ₹50 to ₹200."},
            {"13", "Will my service be disconnected if I miss a payment?", "➜ Yes, after multiple reminders, services may be paused."},
            {"14", "How can I request a payment extension?", "➜ Contact Nvidia Fibernet support for extension requests."},
            {"15", "Can I pay my past-due balance in installments?", "➜ Currently, we do not support installment payments."},
            {"16", "Why is my bill higher than expected?", "➜ Possible reasons: late fees, extra data usage, or plan upgrades."},
            {"17", "Are there any hidden charges in my bill?", "➜ No, all charges are mentioned in the billing section."},
            {"18", "How is the tax calculated on my bill?", "➜ Taxes are applied based on government regulations."},
            {"19", "What are the additional service fees?", "➜ Fees apply for installation, late payments, and extra usage."},
            {"20", "Can I get a detailed breakdown of my charges?", "➜ Yes, detailed bills are available in 'Billing History'."},
            {"21", "Am I eligible for a refund if I was overcharged?", " ➜ Yes, refunds are processed for valid claims."},
          {"22", "How long does it take to process a refund?", " ➜ Refunds are processed within 5-7 business days."},
          {"23", "What should I do if I find an incorrect charge?", " ➜ Contact customer support to dispute incorrect charges."},
          {"24", "Can I dispute a charge on my bill?", " ➜ Yes, raise a billing dispute via your account portal."},
          {"25", "Will I get a refund if I cancel my service?", " ➜ Refunds depend on the cancellation policy of your plan."},
          {"26", "How is my Nvidia Fibernet plan billed?", " ➜ Billing is monthly, but annual plans offer discounts."},
          {"27", "What happens if I upgrade or downgrade my plan?", " ➜ Plan changes take effect in the next billing cycle."},
          {"28", "Is there a contract for my plan?", " ➜ No contracts, you can cancel anytime."},
          {"29", "How can I change my billing cycle?", " ➜ Currently, billing cycles cannot be changed."},
          {"30", "Are there any discounts for long-term subscriptions?", " ➜ Yes, yearly plans have up to 15% discount."},
          {"31", "Does Nvidia Fibernet offer student or senior discounts?", " ➜ Yes, we offer discounts for students and seniors."},
          {"32", "How can I apply for a promotional offer?", " ➜ Apply promo codes at checkout during payment."},
          {"33", "Will my bill increase after the promo period?", " ➜ Yes, after the promo ends, regular rates apply."},
          {"34", "Are there any loyalty rewards?", " ➜ Yes, long-term customers get loyalty benefits."},
          {"35", "Can I combine multiple discounts?", " ➜ No, only one discount can be applied per bill."},
          {"36", "My payment failed. What should I do?", " ➜ Retry after checking your internet and bank details."},
          {"37", "I can't access my billing portal. How do I fix this?", " ➜ Clear browser cache or try a different browser."},
          {"38", "Who should I contact for billing issues?", " ➜ Reach out to Nvidia Fibernet customer support."},
          {"39", "Can I get a copy of my lost bill?", " ➜ Yes, download past bills from your account."},
          {"40", "What happens if my service is suspended?", " ➜ Pay the due balance to restore services."}
        };

        faqPanel.removeAll();
        for (String[] faq : faqs) {
            addFaqToPanel(faq[0], faq[1], faq[2]);
        }
        faqPanel.revalidate();
        faqPanel.repaint();
    }

    
    private void filterFAQs(String text) {
        faqPanel.removeAll(); // Clear previous content

        for (String[] faq : faqs) {
            String question = faq[1].toLowerCase();
            String answer = faq[2].toLowerCase();

            if (question.contains(text) || answer.contains(text)) {
                addFaqToPanel(faq[0], faq[1], faq[2]);
            }
        }

        faqPanel.revalidate();
        faqPanel.repaint();
    }
    private void addFaqToPanel(String number, String question, String answer) {
        JPanel faqItem = new JPanel();
        faqItem.setLayout(new BorderLayout());
        faqItem.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel questionLabel = new JLabel("<html><b>" + number + ". " + question + "</b></html>");
        JLabel answerLabel = new JLabel("<html>" + answer + "</html>");

        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        answerLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        faqItem.add(questionLabel, BorderLayout.NORTH);
        faqItem.add(answerLabel, BorderLayout.CENTER);
        faqItem.setMaximumSize(new Dimension(1366, 100));

        faqPanel.add(faqItem);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FaqDisplay::new);
    }
}